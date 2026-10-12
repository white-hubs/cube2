package com.guhecloud.platform.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("platform.feign.security")
public class FeignSecurityProperties {

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 超时时间
     */
    private Integer timeout;
}
