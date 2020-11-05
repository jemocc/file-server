package com.cc.fileserver.server.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.fileserver.dao.FileIndexDao;
import com.cc.fileserver.bean.entity.FileIndex;
import com.cc.fileserver.server.FileServer;
import com.cc.fileserver.util.FileUtil;
import com.cc.fileserver.util.PublicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Classname FileServerImpl
 * @Description TODO
 * @Date 2020/7/18 18:13
 * @Created by Administrator
 */
@Service
public class FileServerImpl implements FileServer {
    private static final Logger log = LoggerFactory.getLogger(FileServerImpl.class);
    private final DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final FileIndexDao fileIndexDao;

    public FileServerImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public JSONObject saveFile(List<MultipartFile> files) {
        LocalDateTime now = LocalDateTime.now();
        JSONObject rsp = new JSONObject();
        rsp.put("code", RspStatus.FAILURE.getCode());
        rsp.put("data", new JSONArray(files.size()));

        for (MultipartFile file : files) {
            String fileName = PublicUtil.getTimeStamp() + "_" + file.getOriginalFilename();
            String path = PublicUtil.getTempDir() + File.separator + date.format(now) + File.separator + fileName;
            File toFile = new File(path);

            if (!toFile.getParentFile().exists()){
                if (!toFile.getParentFile().mkdirs()){
                    rsp.put("msg", "Create new save path failure.");
                    return rsp;
                }
            }
            try {
                file.transferTo(toFile);    //保存文件
                log.info("Upload fileName: {}, path: {}", file.getOriginalFilename(), path);
                FileIndex fileIndex = fileIndexDao.save(new FileIndex(file.getOriginalFilename(), path));   //保存文件记录
                if (fileIndex == null){
                    rsp.put("msg", "Save file failure.");
                    return rsp;
                } else {
                    rsp.getJSONArray("data").add(fileIndex);
                }
            } catch (IOException e) {
                e.printStackTrace();
                rsp.put("msg", "Save file failure.");
                return rsp;
            }
        }
        rsp.put("code", RspStatus.OK.getCode());
        return rsp;
    }

    @Override
    public void saveFile(Long id, String url) {
        FileIndex fileIndex = fileIndexDao.find(id);
        File oldFile = new File(fileIndex.getPath());
        if (oldFile.delete()){
            log.info("Delete old file: {}", fileIndex.getPath());
            fileIndexDao.del(fileIndex.getId());
        }
        LocalDateTime now = LocalDateTime.now();
        String path = PublicUtil.getTempDir() + File.separator + date.format(now);
        String fileName = PublicUtil.getTimeStamp() + "_" + fileIndex.getName();
        try {
            String newFilePath = FileUtil.downloadFile(url, path, fileName);
            fileIndexDao.save(new FileIndex(fileIndex.getName(), newFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public FileIndex findFileIndex(Long id) {
        return fileIndexDao.find(id);
    }

    @Override
    public void readFileStream(String path, OutputStream os) {
        File f = new File(path);
        if (f.exists()){
            try (InputStream is = new FileInputStream(f)){
                byte[] t = new byte[4096];
                int c;
                while ((c = is.read(t)) != -1){
                    os.write(t, 0, c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public JSONObject pageQuery(Integer page, Integer size) {
        Integer totalCount = fileIndexDao.getTotalCount();
        List<FileIndex> data = fileIndexDao.pageQuery(page, size);
        return new JSONObject()
                .fluentPut("page", page)
                .fluentPut("size", size)
                .fluentPut("total", totalCount)
                .fluentPut("data", data);
    }
}
