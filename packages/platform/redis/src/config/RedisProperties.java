package com.guhecloud.platform.redis.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis缓存配置属性
 *
 * @author Woody
 * @date 2021/7/1
 * @see RedisConfigurer
 */
@Data
@Configuration
@ConditionalOnClass(RedisTemplate.class)
@ConfigurationProperties("platform.cache.redis")
public class RedisProperties {
    /**
     * 消息监听配置实例
     */
    private final Listener listener = new Listener();

    /**
     * 消息监听配置
     */
    @Data
    public static class Listener {
        /**
         * 恢复时间间隔
         */
        private long recoveryInterval = 10000;

        /**
         * 最大订阅注册等待时间
         */
        private long maxSubscriptionRegistrationWaitingTime = 5000;
    }
}
