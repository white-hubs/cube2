package com.guhecloud.cube.srv.gen.reportmanagement.server;

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
public class ReportConfiguration {

    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;

    @Autowired
    private Environment environment;
    //TODO 修改处 需要和servone的数据源配置相对应否则没法找到数据源
    private static final String datasourceName = "com.guhecloud.cube.srv.gen.reportmanagement";

    @Bean("report")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        log.info("工作汇报sql===============");
        List<SpringLiquibase> springLiquibase = liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
        log.info("工作汇报sql执行结果：{}", springLiquibase);
        return springLiquibase;
    }
}
