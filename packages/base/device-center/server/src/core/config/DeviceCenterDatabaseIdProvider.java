package com.guhecloud.cube.base.device.center.core.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author wql
 * @describe 数据源切换配置
 * @date 2024/3/1 17:56
 */
@Configuration
public class DeviceCenterDatabaseIdProvider {
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.put("PostgreSQL", "pg");
        properties.put("MySQL", "mysql");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
