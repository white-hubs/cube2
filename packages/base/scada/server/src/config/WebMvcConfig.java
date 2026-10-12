package com.guhecloud.kaihe.scada.server.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.guhecloud.kaihe.scada.server.infrastructure.web.servlet.xss.XssEscapeFilter;
import com.guhecloud.kaihe.scada.server.infrastructure.web.servlet.xss.XssStringJsonDeserializer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addDeserializer(String.class, new XssStringJsonDeserializer());
                ((MappingJackson2HttpMessageConverter) converter).getObjectMapper().registerModule(simpleModule);
            }
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    }

    @Bean
    public FilterRegistrationBean<XssEscapeFilter> xssEscapeFilterRegistration() {
        FilterRegistrationBean<XssEscapeFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssEscapeFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}
