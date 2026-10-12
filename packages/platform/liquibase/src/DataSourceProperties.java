package com.guhecloud.platform.liquibase;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(
        prefix = "spring.datasource.dynamic"
)
@Data
public class DataSourceProperties {
    private Map<String, DataSourceProperty> datasource;
}
