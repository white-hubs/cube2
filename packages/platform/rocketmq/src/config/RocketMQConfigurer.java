package com.guhecloud.platform.rocketmq.config;

import com.guhecloud.platform.rocketmq.converter.WrappedRocketMQMessageConverter;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
@ConditionalOnClass(RocketMQTemplate.class)
public class RocketMQConfigurer {

    static {
        System.setProperty("mq.consoleAppender.enabled", "true");
    }

    @Bean
    @Primary
    public WrappedRocketMQMessageConverter wrappedRocketMQMessageConverter(JwtDecoder jwtDecoder) {
        return new WrappedRocketMQMessageConverter(jwtDecoder);
    }

}
