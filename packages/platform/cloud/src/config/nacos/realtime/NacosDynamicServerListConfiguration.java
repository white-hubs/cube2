package com.guhecloud.platform.cloud.config.nacos.realtime;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NacosDynamicServerListConfiguration {
    @Bean
    public NacosDynamicServerListUpdater dynamicServerListUpdater(NacosDiscoveryProperties properties) throws NacosException {
        return new NacosDynamicServerListUpdater(NacosFactory.createNamingService(properties.getServerAddr()), properties);
    }
}
