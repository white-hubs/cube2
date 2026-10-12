package com.guhecloud.cube.srv.gen.attendance.management.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(
        prefix = "spring.datasource.dynamic"
)
@Data
public class DataSourceProperties {
    private Map<String, DataSourceProperty> datasource;
}
