package com.cc.fileserver.config;

import com.cc.fileserver.util.PublicUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @Classname JdbcTemplateCfg
 * @Description TODO
 * @Date 2020/7/18 16:40
 * @Created by Administrator
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.h2")
public class H2JdbcTemplateCfg {
    private String url;
    private String username;
    private String password;
    private String driver;

    @Bean("H2DataSource")
    DataSource h2DataSource(){
        url = url.replace("~", PublicUtil.getTempDir());
        return DataSourceBuilder.create().url(url).username(username).password(password).driverClassName(driver).build();
    }

    @Bean("H2JdbcTemplate")
    JdbcTemplate h2JdbcTemplate(@Qualifier("H2DataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

}
