package com.guhecloud.platform.liquibase;

import lombok.Data;

@Data
public class DataSourceProperty {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
