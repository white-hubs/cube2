package com.guhecloud.cube.base.message2.server.config;

import com.guhecloud.platform.liquibase.LiquiBaseDataSourceConfig;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@Configuration
@Slf4j
public class MessageConfiguration {
    @Autowired
    private Environment environment;
    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    private static final String datasourceName = "com.guhecloud.cube.base.message2";

    @Bean("message")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        log.info("message服务开始执行增量sql");
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
