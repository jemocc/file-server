package com.cc.fileserver.dao.impl;

import com.cc.fileserver.dao.FileIndexDao;
import com.cc.fileserver.db_entity.FileIndex;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * @Classname FileIndexImpl
 * @Description TODO
 * @Date 2020/7/20 9:45
 * @Created by Administrator
 * CREATE TABLE PUBLIC.FILE_INDEX (
 *    ID BIGINT AUTO_INCREMENT,
 *    NAME VARCHAR(100),
 *    PATH VARCHAR(255),
 *    CREATE_AT TIMESTAMP,
 *    PRIMARY KEY (`ID`)
 * )
 */
@Repository
public class FileIndexImpl implements FileIndexDao {
    private final JdbcTemplate jdbcTemplate;

    public FileIndexImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public FileIndex save(FileIndex fileIndex) {
        if (fileIndex.getId() == null){
            return create(fileIndex);
        }
        return null;
    }

    private FileIndex create(FileIndex fileIndex){
        String sql = "INSERT INTO PUBLIC.FILE_INDEX (NAME, PATH, CREATE_AT) VALUES (?, ?, ?)";
        Object[] args = new Object[]{fileIndex.getName(), fileIndex.getPath(), Timestamp.from(Instant.now())};
        int r = jdbcTemplate.update(sql, args);
        if (r > 0){
            String sq = "SELECT * FROM PUBLIC.FILE_INDEX WHERE PATH = ? LIMIT 1";
            return jdbcTemplate.query(sq, new Object[]{fileIndex.getPath()}, new BeanPropertyRowMapper<>(FileIndex.class)).get(0);
        }
        return null;
    }

    private FileIndex update(FileIndex fileIndex){
        return null;
    }

    @Override
    public FileIndex find(Long id) {
        String sql = "SELECT * FROM PUBLIC.FILE_INDEX WHERE ID = ? LIMIT 1";
        return jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(FileIndex.class)).get(0);
    }

    @Override
    public int del(Long id) {
        String sql = "DELETE FROM PUBLIC.FILE_INDEX WHERE ID = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<FileIndex> pageQuery(Integer page, Integer size) {
        String sql = "SELECT * FROM PUBLIC.FILE_INDEX ORDER BY ID DESC LIMIT ?,?";
        return jdbcTemplate.query(sql, new Object[]{(page - 1) * size, size}, new BeanPropertyRowMapper<>(FileIndex.class));
    }

    @Override
    public Integer getTotalCount() {
        String sql = "SELECT COUNT(*) FROM PUBLIC.FILE_INDEX";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
