package com.guhecloud.cube.srv.gen.loose.works.config;

import com.guhecloud.platform.liquibase.LiquiBaseDataSourceConfig;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import jakarta.annotation.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * @author wql
 * @describe 数据库自动升级
 * @date 2023/9/21 9:10
 */
@Slf4j
@Configuration
public class LooseWorksConfiguration {
    @Resource
    private Environment environment;
    @Resource
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    private static final String datasourceName = "com.guhecloud.cube.srv.gen.loose.works";

    @Bean("looseWorksConfig")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        log.info("flow开始读取增量sql自动执行配置...");
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
