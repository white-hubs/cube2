package com.guhecloud.platform.liquibase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataSourceConfig {
    private String driver;
    private String url;
    private String username;
    private String password;
}
