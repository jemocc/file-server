package com.cc.fileserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.fileserver.db_entity.FileIndex;
import com.cc.fileserver.plat_entity.Rsp;
import com.cc.fileserver.server.FileServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Classname FileCtrl
 * @Description TODO
 * @Date 2020/7/18 16:22
 * @Created by Administrator
 */
@RestController
@RequestMapping("/api")
public class FileCtrl {

    private static final Logger log = LoggerFactory.getLogger(FileCtrl.class);
    private final FileServer fileServer;

    public FileCtrl(FileServer fileServer) {
        this.fileServer = fileServer;
    }

    @RequestMapping("/upload")
    public Rsp upload(@RequestParam MultipartFile file){
        if (file.isEmpty()){
            return Rsp.failure("Upload File is empty.");
        }
        List<MultipartFile> files = new ArrayList<>(1);
        files.add(file);
        JSONObject rsp = fileServer.saveFile(files);
        return Rsp.build().code(rsp.getInteger("code")).body(rsp.get("data"));
    }

    @RequestMapping("/upload-only-office")
    public JSONObject uploadOnlyOffice(HttpServletRequest request){
        System.out.print("===saveEditedFile------------    ") ;
        JSONObject r = new JSONObject();
        try (Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A")){
            String body = scanner.hasNext() ? scanner.next() : "";
            System.out.println(body);
            JSONObject req = JSONObject.parseObject(body);
            if(req.getInteger("status") == 2 || req.getInteger("status") == 6){
                Long id = req.getLong("key");
                String url = req.getString("url");
                fileServer.saveFile(id, url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        r.put("error", 0);
        return r;
    }

    @RequestMapping("/batch")
    public Rsp batch(HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        if (files.isEmpty()){
            return Rsp.failure("Upload File is empty.");
        }
        JSONObject rsp = fileServer.saveFile(files);
        return Rsp.build().code(rsp.getInteger("code")).body(rsp.get("data"));
    }


    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response){
        Long id = Long.valueOf(request.getParameter("id"));
        try (OutputStream os = response.getOutputStream()){
            FileIndex fileIndex = fileServer.findFileIndex(id);
            log.info("Download fileId: {}, fileName: {}", id, fileIndex.getName());
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    java.net.URLEncoder.encode(fileIndex.getName(), "UTF-8"));
            fileServer.readFileStream(fileIndex.getPath(), os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/query")
    public Rsp query(@RequestBody JSONObject req){
        Integer page = req.getInteger("page");
        Integer size = req.getInteger("size");
        if (size == null)
            size = 10;
        JSONObject r = fileServer.pageQuery(page, size);
        return Rsp.ok().body(r);
    }
}
