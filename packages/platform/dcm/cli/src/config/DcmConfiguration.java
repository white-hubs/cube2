package com.guhecloud.platform.dcm.config;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.guhecloud.platform.base.http.converter.MappingJackson2YamlHttpMessageConverter;
import com.guhecloud.platform.base.http.converter.RawInputStreamHttpMessageConverter;
import feign.Logger;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class DcmConfiguration {

    @Value("${feign.logger-level: BASIC}")
    private String feignLoggerLevel;

    @Value("${dcm.default-providers}")
    private String defaultProviders;

    /**
     * return a bean to support using raw input stream as request body.
     */
    @Bean
    public RawInputStreamHttpMessageConverter rawInputStreamHttpMessageConverter() {
        return new RawInputStreamHttpMessageConverter();
    }

    @Bean
    public MappingJackson2YamlHttpMessageConverter mappingJackson2YamlHttpMessageConverter() {
        return new MappingJackson2YamlHttpMessageConverter(new YAMLMapper());
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        // 不使用@FeignClient注解，通过原有的profile约定指定配置的方案待研究，先用这种方式支持配置
        try {
            return Enum.valueOf(Logger.Level.class, feignLoggerLevel);
        } catch (RuntimeException re) {
            return Logger.Level.BASIC;
        }
    }

    @Bean
    Logger feignLogger() {
        return new Slf4jLogger();
    }

    @Bean
    List<String> defaultProviders() {
        return Arrays.stream(defaultProviders.split(","))
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }
}
