package com.guhecloud.cube.srv.gen.safety.management.config;

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
 * @describe 安全管理增量sql管理
 * @date 2023/9/26 19:00
 */
@Slf4j
@Configuration
public class SafetyManagementConfiguration {
    @Resource
    private Environment environment;
    @Resource
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    private static final String datasourceName = "com.guhecloud.cube.srv.gen.safety.management";

    @Bean("safety-management")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        log.info("safetyManagement开始读取增量sql自动执行配置...");
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
