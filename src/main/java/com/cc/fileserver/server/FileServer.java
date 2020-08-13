package com.cc.fileserver.server;

import com.alibaba.fastjson.JSONObject;
import com.cc.fileserver.db_entity.FileIndex;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;

/**
 * @InterfaceName FileServer
 * @Description TODO
 * @Date 2020/7/18 18:12
 * @Created by Administrator
 */
public interface FileServer {
    JSONObject saveFile(List<MultipartFile> files);

    void saveFile(Long id, String url);

    FileIndex findFileIndex(Long id);

    void readFileStream(String path, OutputStream os);

    JSONObject pageQuery(Integer page, Integer size);
}
