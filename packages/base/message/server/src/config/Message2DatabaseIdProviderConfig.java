package com.guhecloud.cube.base.message2.server.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-05  15:22
 * @Description:
 * @Version: 1.0
 */
@Configuration
public class Message2DatabaseIdProviderConfig {
    @Bean
    @ConditionalOnMissingBean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.put("PostgreSQL", "pg");
        properties.put("MySQL", "mysql");
        properties.put("DM DBMS", "dm");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
