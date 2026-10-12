package com.guhecloud.cube.srv.gen.device.supervise.server.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-05  16:01
 * @Description:
 * @Version: 1.0
 */
@Configuration
public class DeviceSuperviseDatabaseIdProviderConfig {
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
