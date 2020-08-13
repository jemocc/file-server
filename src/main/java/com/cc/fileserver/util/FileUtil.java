package com.cc.fileserver.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Classname FileUtil
 * @Description TODO
 * @Date 2020/7/10 9:33
 * @Created by Administrator
 */
public class FileUtil {
    private final static Logger log = LoggerFactory.getLogger(FileUtil.class);
    private final static String BOUNDARY = "----" + UUID.randomUUID().toString().replaceAll("-", "");
    private final static String PREFIX = "--";
    private final static String CRLF = "\r\n";

    public static String downloadFile(String url, String savePath, String fileName) throws IOException {
        savePath = savePath.replaceAll("/?$", "");
        File file = new File(savePath + File.separator + fileName);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                log.error("Create new file {} failure.", file.getAbsoluteFile());
            }
        }
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        try (
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), 4096);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))
        ) {
            byte[] tb = new byte[4096];
            int c;
            while((c = bis.read(tb)) != -1){
                bos.write(tb, 0, c);
            }
            conn.disconnect();
            log.info("Download remote file {} success.", file.getAbsoluteFile());
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] readBytes(BufferedInputStream bis) throws IOException {
        byte[] tb = new byte[4096];
        int s = 0;
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        int t = 0;
        while ((t = bis.read(tb)) != -1) {
            aos.write(tb, 0, t);
            s += t;
        }
        return aos.toByteArray();
    }

    public static String uploadFile(String url, Map<String, File> files, Map<String, String> param, String authorization) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        conn.setRequestProperty("Cookie", "XSRF-TOKEN=" + authorization);
        DataOutputStream dos = null;
        BufferedInputStream bis = null;
        try {
            dos = new DataOutputStream(conn.getOutputStream());
            writeFiles(files, dos);
            writeParam(param, dos);
            writ0(PREFIX + BOUNDARY + PREFIX + CRLF, dos);
            dos.flush();

            System.out.println("ResponseCode: " + conn.getResponseCode()
                    + ", ResponseMessage: " + conn.getResponseMessage());
            if (conn.getResponseCode() != 200) {
                JSONObject r = new JSONObject();
                r.put("code", conn.getResponseCode());
                r.put("msg", conn.getResponseMessage());
                return r.toJSONString();
            }
            bis = new BufferedInputStream(conn.getInputStream());
            byte[] rbs = readBytes(bis);
            return new String(rbs);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(dos);
            close(bis);
        }
    }

    private static void writeParam(Map<String, String> param, DataOutputStream dos) throws IOException {
        Set<Map.Entry<String, String>> entries = param.entrySet();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : entries) {
            sb.append(PREFIX).append(BOUNDARY).append(CRLF);
            sb.append(String.format("Content-Disposition: form-data; name=\"%s\"", entry.getKey())).append(CRLF);
            sb.append(CRLF);
            sb.append(entry.getValue());
            sb.append(CRLF);
        }
        writ0(sb.toString(), dos);
    }

    private static void writeFiles(Map<String, File> files, DataOutputStream dos) throws IOException {
        Set<Map.Entry<String, File>> entries = files.entrySet();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, File> entry : entries) {
            sb.append(PREFIX).append(BOUNDARY).append(CRLF);
            sb.append(
                    String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"",
                            entry.getKey(),
                            entry.getValue().getName()
                    )
            ).append(CRLF);
//            sb.append("Content-Type: application/x-pdf").append(CRLF);
            sb.append(CRLF);

            writ0(sb.toString(), dos);
            writeFileData(entry.getValue(), dos);
            writ0(CRLF, dos);
        }
    }

    private static void writeFileData(File f, DataOutputStream dos) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            byte[] buffer = new byte[1024 * 256];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, len);
            }
            System.out.print("(binary)");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(fis);
        }

    }

    private static void writ0(String data, DataOutputStream dos) throws IOException {
        System.out.print(data);
        dos.writeBytes(data);
    }

    private static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
