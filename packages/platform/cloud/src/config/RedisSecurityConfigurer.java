package com.guhecloud.platform.cloud.config;

import com.guhecloud.platform.redis.signature.RedisSigner;
import com.guhecloud.platform.redis.token.RedisJwtActiveValidator;
import com.guhecloud.platform.redis.token.RedisTokenReplayValidator;
import com.guhecloud.platform.security.signature.Signer;
import com.guhecloud.platform.security.token.JwtActiveValidator;
import com.guhecloud.platform.security.token.TokenReplayValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于Redis的安全相关配置
 *
 * @author Woody
 * @date 2021/7/5
 * @see com.guhecloud.platform.security.config.WebSecurityConfigurer
 */
@Configuration
@ConditionalOnClass(RedisTemplate.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisSecurityConfigurer {
    private final RedisTemplate<String, Object> redisTemplate;

    @Bean
    @ConditionalOnMissingBean(Signer.class)
    @ConditionalOnProperty(prefix = "platform.security.signature", name = "enable", matchIfMissing = true)
    public Signer signer() {
        return new RedisSigner();
    }

    @Bean
    @ConditionalOnMissingBean(JwtActiveValidator.class)
    public JwtActiveValidator redisJwtActiveValidator() {
        return new RedisJwtActiveValidator(this.redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(TokenReplayValidator.class)
    public TokenReplayValidator tokenReplayValidator() {
        return new RedisTokenReplayValidator(this.redisTemplate);
    }
}
