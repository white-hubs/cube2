package com.guhecloud.cube.srv.gen.library.management.common.config;

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
 * 自动sql配置
 */
@Configuration
public class libraryManagementLiquiBaseConfig {
    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    @Autowired
    private Environment environment;
    //todo：此处需要和servone的数据源配置相对应否则没法找到数据源
    private static final String datasourceName = "com.guhecloud.cube.srv.gen.library.management";

    @Bean("libraryManagementLiquiConfig")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
