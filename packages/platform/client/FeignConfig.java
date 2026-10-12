package com.guhecloud.platform.client;

import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
