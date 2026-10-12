package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.config;

import com.guhecloud.platform.liquibase.LiquiBaseDataSourceConfig;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * liquiBase配置类
 */
@Configuration
public class DisinfectionLiquiBaseConfig {
    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    @Autowired
    private Environment environment;
    private static final String datasourceName = "com.guhecloud.cube.srv.gen.disinfection";

    @Bean("disinfection")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
