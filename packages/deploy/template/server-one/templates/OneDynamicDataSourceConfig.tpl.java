package com.guhecloud.cube.servone.config;

import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class OneDynamicDataSourceConfig {

    private final DynamicDataSourceProperties properties;

    private static final Set<String> limitedDataSources = new HashSet<>(Arrays.asList(
            // @formatter:off
            {DEP_DATA_SOURCE_PACKAGES}
            // @formatter:on
            ""
                                                                                     ));

    @PostConstruct
    void afterPropertiesSet() {
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource().entrySet()
                .stream().filter(x -> limitedDataSources.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        properties.setDatasource(datasourceMap);
    }
}