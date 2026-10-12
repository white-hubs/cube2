package com.guhecloud.cube.srv.gen.securitymanagement.server.common.config;

import lombok.Data;

@Data
public class SecurityManagementClickHouseConfigProperties {
    private String jdbcUrl;
    private String driverClassName;
    private String username;
    private String password;
}
