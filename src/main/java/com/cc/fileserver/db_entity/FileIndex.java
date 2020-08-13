package com.cc.fileserver.db_entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class FileIndex implements Serializable {
    private Long id;
    private String name;
    private String path;
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
