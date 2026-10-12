package com.guhecloud.cube.srv.gen.safety.personnel.server;

import com.guhecloud.platform.liquibase.LiquiBaseDataSourceConfig;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * 安全生产统一智管平台人员管理服务配置。
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SafetyPersonnelConfiguration {

    private static final String DATASOURCE_NAME = "com.guhecloud.cube.srv.gen.safety.personnel";

    private final LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    private final Environment environment;

    /**
     * 加载安全人员服务增量SQL。
     *
     * @param dataSource 当前服务数据源
     *
     * @return Liquibase执行器列表
     */
    @Bean("safetyPersonnelLiquibase")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        log.info("safetyPersonnel开始读取增量sql自动执行配置...");
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, DATASOURCE_NAME);
    }
}
