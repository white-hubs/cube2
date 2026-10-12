package com.guhecloud.cube.base.bracing.config;

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
 * @describe 消息支撑服务增量sql执行
 * @date 2023/9/26 19:11
 */
@Slf4j
@Configuration
public class BracingConfiguration {
    @Resource
    private Environment environment;
    @Resource
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    //此处需要和servOne的数据源配置相对应否则没法找到数据源
    private static final String datasourceName = "com.guhecloud.cube.base.bracing";

    @Bean("bracing")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        log.info("bracing开始读取增量sql自动执行配置...");
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
