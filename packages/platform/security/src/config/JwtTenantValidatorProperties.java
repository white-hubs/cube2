package com.guhecloud.platform.security.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Configuration
@ConfigurationProperties(prefix = "platform.security.jwt.tenant-validator")
public class JwtTenantValidatorProperties {

    /**
     * 忽略的地址前缀
     */
    private List<String> ignoreUriPrefixes = new ArrayList<>();

    /**
     * 忽略的地址
     */
    private List<String> ignoreUris = new ArrayList<>();

    /**
     * 忽略的端
     */
    private List<String> ignoreTerminals = new ArrayList<>();
}
