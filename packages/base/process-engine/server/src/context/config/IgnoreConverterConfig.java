package com.guhecloud.cube.base.flow.context.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgnoreConverterConfig {

    /**
     * 因activiti自动注册的convert导致的platform自定义的convert失效问题，故而删除
     *
     * @return
     */
    @Bean
    public BeanDefinitionRegistryPostProcessor removeConverter() {
        return registry -> {
            if (registry.containsBeanDefinition("stringToLocalDateTimeConverter")) {
                registry.removeBeanDefinition("stringToLocalDateTimeConverter");
            }
        };
    }
}
