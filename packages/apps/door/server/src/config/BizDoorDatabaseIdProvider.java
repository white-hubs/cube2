package com.guhecloud.cube.biz.door.server.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class BizDoorDatabaseIdProvider {
    @Bean
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
