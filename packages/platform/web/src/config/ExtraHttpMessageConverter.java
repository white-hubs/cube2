package com.guhecloud.platform.base.config;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.guhecloud.platform.base.http.converter.MappingJackson2YamlHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ExtraHttpMessageConverter implements WebMvcConfigurer {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2YamlHttpMessageConverter yamlHttpMessageConverter = new MappingJackson2YamlHttpMessageConverter(new YAMLMapper());
        converters.add(yamlHttpMessageConverter);
    }
}
