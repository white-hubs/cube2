package com.guhecloud.cube.servone.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Configuration
@AutoConfigureBefore({ DynamicDataSourceAutoConfiguration.class, SpringBootConfiguration.class })
public class DataSourceConfiguration {

    /**
     * 分库分表数据源名称，和配置文件里的一致
     */
    public static final String SHARDING_DATA_SOURCE_NAME = "com.guhecloud.cube.base.file";

    /**
     * 多数据源配置
     */
    private final DynamicDataSourceProperties properties;

    @Autowired
    private DefaultDataSourceCreator defaultDataSourceCreator;

    public DataSourceConfiguration(DynamicDataSourceProperties properties) {
        this.properties = properties;
    }

    /**
     * 将分库分表数据源加入多数据源（dataSourceMap）中
     */
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        // 使用这个方法，确保是在properties的fix之后产生DynamicDataSourceProvider
        return new YmlDynamicDataSourceProvider(defaultDataSourceCreator, properties.getDatasource()) {
            @Override
            public Map<String, DataSource> loadDataSources() {
                return createDataSourceMap(properties.getDatasource());
            }
        };
    }

    @Primary
    @Bean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource(List.of(dynamicDataSourceProvider));
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }
}
