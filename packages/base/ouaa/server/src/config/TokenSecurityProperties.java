package com.guhecloud.cube.biz.ouaa.server.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.authorizationserver.token")
public class TokenSecurityProperties {

    /**
     * 令牌密钥对配置实例
     */
    private final KeyPair keyPair = new KeyPair();

    /**
     * 令牌密钥对配置
     */
    @Data
    public static class KeyPair {
        /**
         * 密钥别名
         */
        private String alias;

        /**
         * 资源路径
         */
        private String resource;

        /**
         * 密钥对密码
         */
        private String keyPairPassword;

        /**
         * 密钥存储密码
         */
        private String keyStorePassword;
    }

}
