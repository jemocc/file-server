package com.cc.fileserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.aspose.words.UnsupportedFileFormatException;
import com.cc.fileserver.bean.entity.FileIndex;
import com.cc.fileserver.bean.Const;
import com.cc.fileserver.bean.pojo.Rsp;
import com.cc.fileserver.server.FileCoverServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Classname FileCoverCtrl
 * @Description TODO
 * @Date 2020/8/6 18:55
 * @Created by Administrator
 */
@RestController
@RequestMapping("/api")
public class FileCoverCtrl {
    private static final Logger log = LoggerFactory.getLogger(FileCoverCtrl.class);
    private final FileCoverServer fileCoverServer;

    public FileCoverCtrl(FileCoverServer fileCoverServer) {
        this.fileCoverServer = fileCoverServer;
    }

    @PostMapping("/word2pdf")
    public Rsp word2pdf(@RequestBody JSONObject request){
        log.info("<<< {}", request.toJSONString());
        String url = request.getString("url");
        FileIndex index = null;
        try {
            index = fileCoverServer.word2pdf(url);
            if (index != null){
                JSONObject body = new JSONObject();
                String downUrl = Const.HOST + "/api/download?id=" + index.getId();
                body.put("url", downUrl);
                log.info(">>> {}\n" + body.toJSONString());
                return Rsp.ok().body(body);
            } else {
                return Rsp.failure("转换失败");
            }
        } catch (UnsupportedFileFormatException e) {
            return Rsp.failure("不支持的文件类型");
        }
    }
}
