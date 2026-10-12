package com.guhecloud.platform.redis.token;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.redis.config.RedisJwtActiveStoreProperties;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.token.JwtActiveValidator;
import com.guhecloud.platform.security.util.TokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Objects;

@Slf4j
public class RedisJwtActiveValidator implements JwtActiveValidator {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisJwtActiveValidator(@NonNull RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public OAuth2TokenValidatorResult validate(@NonNull Jwt jwt) {
        // 客户端的授权token无需进行redis存储（参见 RedisTokenStore），此处直接返回检验通过
        if (SecurityContextHolder.isClient()) {
            return OAuth2TokenValidatorResult.success();
        }
        return this.validateUserJwt(jwt);
    }

    private OAuth2TokenValidatorResult validateUserJwt(Jwt jwt) {
        var redisJwtActiveStoreProperties = RedisJwtActiveStoreProperties.getConfig();
        // 验证Redis中是否存在该令牌，同时忽略Redis请求/操作异常（为了提高系统可用性）
        var terminal = SessionContextHolder.getRequesterProperty(Requester::getTerminal);
        var key = TokenUtils.key(TokenUtils.getAccount(jwt));
        if (log.isDebugEnabled()) {
            log.debug("Validating redis token key: {}", key);
        }
        try {
            if (redisJwtActiveStoreProperties.getCrossTokenTerminalValid()) {
                var token = (String) this.redisTemplate.opsForHash().get(key, terminal + ":" + TokenUtils.getJti(jwt));
                if (Objects.isNull(token)) {
                    return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Token is not active", null));
                }
            } else {
                var hKeys = this.redisTemplate.opsForHash().keys(key);
                if (CollectionUtils.isEmpty(hKeys) ||
                    hKeys.stream().noneMatch(hKey -> hKey.toString().contains(TokenUtils.getJti(jwt)))) {
                    return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Token is not active", null));
                }
            }
        } catch (Exception e) {
            log.warn("Token validate failed: {}", e.getMessage());
        }
        return OAuth2TokenValidatorResult.success();
    }
}
