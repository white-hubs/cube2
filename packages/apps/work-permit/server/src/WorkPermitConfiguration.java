package com.guhecloud.cube.srv.gen.work.permit.server;

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

@Slf4j
@Configuration
public class WorkPermitConfiguration {

    private static final String DATASOURCE_NAME = "com.guhecloud.cube.srv.gen.work.permit";

    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;

    @Autowired
    private Environment environment;

    @Bean("workPermitLiquibase")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        log.info("作业管理sql===============");
        List<SpringLiquibase> springLiquibase = liquiBaseDataSourceConfig.getSpringLiquibase(environment, DATASOURCE_NAME);
        log.info("作业管理sql执行结果：{}", springLiquibase);
        return springLiquibase;
    }
}
