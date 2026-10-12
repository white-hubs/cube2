package com.guhecloud.platform.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.platform.base.standard.jackson.ObjectMapperConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson全局配置
 *
 * @author Woody
 * @date 2021/5/28
 */
@Configuration
public class JacksonConfigurer {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return ObjectMapperConfig.getDefaultObjectMapper();
    }

}
