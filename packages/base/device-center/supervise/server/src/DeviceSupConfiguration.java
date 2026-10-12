package com.guhecloud.cube.srv.gen.device.supervise.server;

import com.guhecloud.platform.liquibase.LiquiBaseDataSourceConfig;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import jakarta.annotation.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * @author wql
 * @describe 增量sql自动执行
 * @date 2024/3/12 9:03
 */
@Configuration
public class DeviceSupConfiguration {
    @Resource
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    @Resource
    private Environment environment;
    private static final String datasourceName = "com.guhecloud.cube.srv.gen.device.supervise";

    @Bean("deviceSupervise")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {

        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
