package com.cc.fileserver.server.impl;

import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.UnsupportedFileFormatException;
import com.cc.fileserver.dao.FileIndexDao;
import com.cc.fileserver.bean.entity.FileIndex;
import com.cc.fileserver.server.FileCoverServer;
import com.cc.fileserver.util.FileUtil;
import com.cc.fileserver.util.PublicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @Classname FileCoverServerImpl
 * @Description TODO
 * @Date 2020/8/6 19:00
 * @Created by Administrator
 */
@Service
public class FileCoverServerImpl implements FileCoverServer {
    private static final Logger log = LoggerFactory.getLogger(FileServerImpl.class);
    private final FileIndexDao fileIndexDao;

    public FileCoverServerImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    {
        if ("linux".equals(PublicUtil.OS_TYPE)){
            FontSettings.setFontsFolder("/usr/share/fonts/", true);
            log.info("Config FontSettings: setFontsFolder /usr/share/fonts/");
        }
    }
    @Override
    public FileIndex word2pdf(String remoteFileUrl) throws UnsupportedFileFormatException {
        String savePath = PublicUtil.getDefaultSavePath("");
        String fileName = PublicUtil.getTimeStamp() + "";
        try {
            String tp = FileUtil.downloadFile(remoteFileUrl, savePath, fileName);
            if (tp != null){
                Document document = new Document(tp);
                String op = PublicUtil.getDefaultSavePath(fileName + ".pdf");
                document.save(op, com.aspose.words.SaveFormat.PDF);
                File of = new File(tp);
                of.delete();
                return fileIndexDao.save(new FileIndex(fileName + ".pdf", op));
            }
        } catch (UnsupportedFileFormatException e){
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
