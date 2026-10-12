package com.guhecloud.cube.srv.gen.maintain.server;

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
 * @author wql
 * @describe 增量sql自动执行
 * @date 2023/6/19 14:48
 */
@Configuration
public class MaintainConfiguration {
    @Autowired
    private Environment environment;
    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    private static final String datasourceName = "com.guhecloud.cube.srv.gen.maintain";

    @Bean("maintain")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
