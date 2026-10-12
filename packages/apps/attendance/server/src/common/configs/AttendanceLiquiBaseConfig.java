package com.guhecloud.cube.srv.gen.attendance.management.common.configs;

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
 * liquiBase配置类
 */
@Configuration
public class AttendanceLiquiBaseConfig {
    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    @Autowired
    private Environment environment;
    //todo：此处需要和servone的数据源配置相对应否则没法找到数据源
    private static final String datasourceName = "com.guhecloud.cube.srv.gen.attendance.management";

    @Bean("attendance")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
