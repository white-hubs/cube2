package com.guhecloud.cube.gm.meattrade.plugin;

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

@Configuration
@Slf4j
public class MeatTradeGmPluginConfiguration {
    @Autowired
    private LiquiBaseDataSourceConfig liquiBaseDataSourceConfig;
    @Autowired
    private Environment environment;
    //此处需要和servone的数据源配置相对应否则没法找到数据源
    private static final String datasourceName = "com.guhecloud.cube.gm.meattrade.plugin";

    @Bean("gmMeattradePlugin")
    public List<SpringLiquibase> liquibase(DataSource dataSource) throws LiquibaseException, IOException {
        log.info("光明滑轨插件开始读取增量sql自动执行配置...");
        return liquiBaseDataSourceConfig.getSpringLiquibase(environment, datasourceName);
    }
}
