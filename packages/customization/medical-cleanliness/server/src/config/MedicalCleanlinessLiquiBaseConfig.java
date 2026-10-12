package com.guhecloud.cube.srv.gen.medical.cleanliness.server.config;

import com.guhecloud.platform.liquibase.LiquiBaseDataSourceConfig;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-18  10:34
 * @Description: liquibase入口
 * @Version: 1.0
 */
@Configuration
public class MedicalCleanlinessLiquiBaseConfig {
    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    @Autowired
    private Environment environment;
    private static final String datasourceName = "com.guhecloud.cube.srv.gen.medical.cleanliness";
    @Bean("medicalCleanliness")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment,datasourceName);
    }
}
