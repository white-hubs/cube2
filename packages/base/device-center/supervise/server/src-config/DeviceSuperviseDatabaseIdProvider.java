package com.guhecloud.cube.srv.gen.device.supervise.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author wql
 * @describe 国产数据库切换
 * @date 2024/3/4 17:43
 */
@Configuration
public class DeviceSuperviseDatabaseIdProvider {
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
