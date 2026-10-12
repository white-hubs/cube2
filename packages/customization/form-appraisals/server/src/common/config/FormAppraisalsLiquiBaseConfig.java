package com.guhecloud.cube.srv.gen.appraisals.server.common.config;

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

/**
 * 表单考核服务增量 SQL 自动执行配置。
 */
@Slf4j
@Configuration
public class FormAppraisalsLiquiBaseConfig {

    private static final String DATASOURCE_NAME = "com.guhecloud.cube.srv.gen.appraisals";

    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;

    @Autowired
    private Environment environment;

    @Bean("formAppraisalsLiquibase")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        log.info("formAppraisals start execute liquibase increment sql");
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, DATASOURCE_NAME);
    }
}
