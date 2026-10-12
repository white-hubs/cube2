package com.guhecloud.cube.iot.mgt.core.data.config;

import lombok.Data;

@Data
public class ClickHouseConfigProperties {
    private String jdbcUrl;
    private String driverClassName;
    private String username;
    private String password;
}
