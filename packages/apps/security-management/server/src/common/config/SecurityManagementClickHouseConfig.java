package com.guhecloud.cube.srv.gen.securitymanagement.server.common.config;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossClickHouseService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.impl.LineLossClickHouseServiceImpl;
import com.guhecloud.platform.base.util.StringUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SecurityManagementClickHouseConfig {
    @Bean
    public LineLossClickHouseService lineLossClickHouseService(
            SecurityManagementClickHouseConfigProperties securityManagementClickHouseConfigProperties
                                                              ) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(securityManagementClickHouseConfigProperties));
        return new LineLossClickHouseServiceImpl(jdbcTemplate);
    }

    @Bean
    @ConfigurationProperties(prefix = "click-house")
    public SecurityManagementClickHouseConfigProperties securityManagementClickHouseConfigProperties() {
        return new SecurityManagementClickHouseConfigProperties();
    }

    private HikariDataSource getDataSource(SecurityManagementClickHouseConfigProperties securityManagementClickHouseConfigProperties) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(securityManagementClickHouseConfigProperties.getJdbcUrl());
        hikariDataSource.setDriverClassName(securityManagementClickHouseConfigProperties.getDriverClassName());
        if (StringUtils.isNotEmpty(securityManagementClickHouseConfigProperties.getUsername())) {
            hikariDataSource.setUsername(securityManagementClickHouseConfigProperties.getUsername());
            hikariDataSource.setPassword(securityManagementClickHouseConfigProperties.getPassword());
        }
        return hikariDataSource;
    }
}
