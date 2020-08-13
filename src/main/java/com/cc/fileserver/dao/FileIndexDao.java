package com.cc.fileserver.dao;

import com.cc.fileserver.db_entity.FileIndex;

import java.util.List;

/**
 * @InterfaceName FileIndexDao
 * @Description TODO
 * @Date 2020/7/20 9:40
 * @Created by Administrator
 * CREATE TABLE PUBLIC.FILE_INDEX (
 *    ID BIGINT AUTO_INCREMENT,
 *    NAME VARCHAR(100),
 *    PATH VARCHAR(255),
 *    CREATE_AT TIMESTAMP,
 *    PRIMARY KEY (`ID`)
 * )
 */
public interface FileIndexDao {
    FileIndex save(FileIndex fileIndex);

    FileIndex find(Long id);

    int del(Long id);

    List<FileIndex> pageQuery(Integer page, Integer size);

    Integer getTotalCount();
}
