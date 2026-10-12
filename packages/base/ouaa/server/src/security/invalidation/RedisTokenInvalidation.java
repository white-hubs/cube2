package com.guhecloud.cube.biz.ouaa.server.security.invalidation;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.util.TokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RedisTokenInvalidation implements TokenInvalidation {

    private final RedisTemplate<String, Object> redisTemplate;

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    public RedisTokenInvalidation(@NonNull RedisTemplate<String, Object> redisTemplate,
                                  OAuth2AuthorizationService oAuth2AuthorizationService
                                 ) {
        this.redisTemplate = redisTemplate;
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
    }

    @Override
    public void removeToken(String account, String terminal) {
        var key = TokenUtils.key(account);
        if (log.isDebugEnabled()) {
            log.debug("Removing redis token key: {}", key);
        }
        var hKeys = this.redisTemplate.opsForHash().keys(key);
        if (CollectionUtils.isEmpty(hKeys)) {
            return;
        }
        var userTokenTerminalKeys = hKeys.stream().filter(hKey -> hKey.toString().contains(terminal))
                .map(String::valueOf)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(userTokenTerminalKeys)) {
            return;
        }
        for (var hKey : userTokenTerminalKeys) {
            var token = (String) this.redisTemplate.opsForHash().get(key, hKey);
            var accessToken = oAuth2AuthorizationService.findByToken((token), OAuth2TokenType.ACCESS_TOKEN);
            if (Objects.nonNull(accessToken)) {
                this.oAuth2AuthorizationService.remove(accessToken);
            }
        }
    }
}