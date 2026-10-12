package com.guhecloud.cube.customization.xialing.server.liangcang.config;

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
 * 长沙霞凝粮仓（liangcang）Liquibase 配置；增量 SQL 位于
 * {@code classpath:db/com.guhecloud.cube.customization.xialing/*.sql}，由平台 {@link LiquiBaseDataSourceConfig} 扫描执行。
 */
@Configuration
public class LiangcangLiquiBaseConfig {

    /**
     * 与 {@code resources/db/} 下目录名一致（classpath {@code db/<name>/*.sql}）。
     */
    private static final String DATASOURCE_NAME = "com.guhecloud.cube.customization.xialing";

    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    @Autowired
    private Environment environment;

    @Bean("liangcang")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, DATASOURCE_NAME);
    }
}
