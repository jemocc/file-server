package com.cc.fileserver.util;

import com.cc.fileserver.FileServerApplication;
import com.cc.fileserver.server.impl.FileServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Classname PublicUtil
 * @Description TODO
 * @Date 2020/7/18 16:24
 * @Created by Administrator
 */
public class PublicUtil {
    private static final Logger log = LoggerFactory.getLogger(PublicUtil.class);
    public static final String OS_TYPE = System.getProperty("os.name").toLowerCase();
    private static String defaultTempDir = "/";
    private static final DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    static {
        URL p = Thread.currentThread().getContextClassLoader().getResource("");
        if (p != null){
            try {
                defaultTempDir = URLDecoder.decode(p.toString()
                        .replaceAll("^(jar:file:)?", "")
                        .replaceAll("/[^/]*\\.jar.*$", ""),"utf-8");
                log.info("Init defaultTempDir: " + defaultTempDir);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static long getTimeStamp(){ return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli(); }

    public static String getTempDir(){
        String t = null;
        if (OS_TYPE.contains("linux"))
            t = defaultTempDir;
        else if (OS_TYPE.contains("windows"))
            t = "D:\\temp_file";
        return t == null ? defaultTempDir : t;
    }

    public static String getToday(){
        LocalDateTime now = LocalDateTime.now();
        return date.format(now);
    }

    public static String getDefaultSavePath(String fileName){
        return getTempDir() + File.separator + getToday() + File.separator + fileName;
    }
}
