package com.cc.fileserver.bean.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Classname FileIndex
 * @Description TODO
 * @Date 2020/7/20 9:42
 * @Created by Administrator
 * CREATE TABLE PUBLIC.FILE_INDEX (
 *    ID BIGINT AUTO_INCREMENT,
 *    NAME VARCHAR(100),
 *    PATH VARCHAR(255),
 *    CREATE_AT TIMESTAMP,
 *    PRIMARY KEY (`ID`)
 * )
 */
@ApiModel(description = "文件索引")
public class FileIndex implements Serializable {
    @ApiModelProperty(value = "文件ID")
    private Long id;
    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "文件存储位置")
    private String path;
    @ApiModelProperty(value = "创建时间")
    private String createAt;

    public FileIndex() {
    }

    public FileIndex(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "FileIndex{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", createAt='" + createAt + '\'' +
                '}';
    }
}
