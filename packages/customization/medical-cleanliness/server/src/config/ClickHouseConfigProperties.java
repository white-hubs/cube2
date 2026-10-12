package com.guhecloud.cube.srv.gen.medical.cleanliness.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: ClickHouse 数据源配置
 * @author: lijie
 * @date: 2024/9/14 11:49
 **/
@ConfigurationProperties(prefix = "click-house")
@Component
public class ClickHouseConfigProperties {
    private String jdbcUrl;
    private String driverClassName;
    private String username;
    private String password;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
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
}
