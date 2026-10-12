package com.guhecloud.cube.base.visualization.server.config;

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
 * @Author: lijie
 * @CreateTime: 2023-08-31  19:07
 * @Description:
 * @Version: 1.0
 */
@Configuration
@Slf4j
public class VisualizationConfiguration {
    @Autowired
    private Environment environment;
    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    private static final String datasourceName = "com.guhecloud.cube.base.visualization";

    @Bean("visualization")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        log.info("可视化配置服务开始执行增量sql");
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
