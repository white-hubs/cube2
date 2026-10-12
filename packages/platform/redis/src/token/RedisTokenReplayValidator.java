package com.guhecloud.platform.redis.token;

import com.guhecloud.platform.base.util.DigestUtils;
import com.guhecloud.platform.security.interceptor.TokenReplayValidateInterceptor;
import com.guhecloud.platform.security.token.TokenReplayValidator;
import com.guhecloud.platform.security.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.method.HandlerMethod;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的令牌重放验证实现
 *
 * @author Woody
 * @date 2021/6/20
 * @see TokenReplayValidator
 * @see TokenReplayValidateInterceptor
 */
public class RedisTokenReplayValidator implements TokenReplayValidator {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTokenReplayValidator(@NonNull RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isReplayed(HttpServletRequest request, HandlerMethod method, Jwt jwt) {
        if (request == null || method == null || jwt == null) {
            return false;
        }
        // 根据服务名称、请求方法、请求令牌ID构建唯一标识
        String key = DigestUtils.sha256Hex(String.join(":",
                                                       request.getServerName(),
                                                       method.getBeanType().getName(),
                                                       method.getMethod().getName(),
                                                       TokenUtils.getJti(jwt)
                                                      ));
        String operator = TokenUtils.getAccount(jwt);
        long timeout = Objects.requireNonNull(jwt.getExpiresAt()).toEpochMilli() - System.currentTimeMillis();
        ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
        return Boolean.FALSE.equals(operations.setIfAbsent(key, operator, timeout, TimeUnit.MILLISECONDS));
    }
}
