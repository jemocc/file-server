package com.cc.fileserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @Classname DataBaseInit
 * @Description TODO
 * @Date 2020/7/18 17:24
 * @Created by Administrator
 */
@Component
public class DataBaseInitCfg {
    private final Logger log = LoggerFactory.getLogger(DataBaseInitCfg.class);
    private final JdbcTemplate jdbcTemplate;


    public DataBaseInitCfg(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init(){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT t.TABLE_NAME FROM INFORMATION_SCHEMA.TABLES t WHERE TABLE_SCHEMA = 'PUBLIC'");
        if (list.size() == 0){
            String createFileIndexTable = "CREATE TABLE PUBLIC.FILE_INDEX (ID BIGINT AUTO_INCREMENT, NAME VARCHAR(100), PATH VARCHAR(255), CREATE_AT TIMESTAMP, PRIMARY KEY (`ID`))";
            jdbcTemplate.execute(createFileIndexTable);
            log.info("DataBaseInitCfg init [ create ]");
        } else
            log.info("DataBaseInitCfg init [ skip ]");
    }
}
