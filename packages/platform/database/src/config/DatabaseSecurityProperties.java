package com.guhecloud.platform.database.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库操作安全配置
 *
 * @author Woody
 * @date 2021/7/8
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "platform.database")
public class DatabaseSecurityProperties {
    /**
     * 编解码配置实例
     */
    private final Codec codec = new Codec();

    /**
     * 编解码配置
     */
    @Data
    public static class Codec {
        /**
         * 密钥
         */
        private String secret;

        /**
         * 偏移量（16位）
         */
        private String iv;
    }
}
