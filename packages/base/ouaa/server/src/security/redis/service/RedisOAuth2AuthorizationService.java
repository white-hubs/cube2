package com.guhecloud.cube.biz.ouaa.server.security.redis.service;

import com.guhecloud.cube.biz.ouaa.server.security.redis.entity.OAuth2AuthorizationGrantAuthorization;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.redis.config.RedisJwtActiveStoreProperties;
import com.guhecloud.platform.redis.event.TokenRemoveEvent;
import com.guhecloud.platform.security.util.TokenUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RegisteredClientRepository registeredClientRepository;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 授权记录存储
     */
    public static final String AUTHORIZATION_KEY = "oauth2:authorization:{id}";

    /**
     * 访问令牌到授权ID的映射
     */
    public static final String ACCESS_TOKEN_TO_AUTHORIZATION = "oauth2:access_token:{token}";

    /**
     * 刷新令牌到授权ID的映射
     */
    public static final String REFRESH_TOKEN_TO_AUTHORIZATION = "oauth2:refresh_token:{token}";

    /**
     * 用户令牌存储
     */
    public static final String USER_ACCESS_TOKEN_TO_AUTHORIZATION = "oauth2:user_jti:{userJti}";

    public RedisOAuth2AuthorizationService(RedisTemplate<String, Object> redisTemplate,
                                           RegisteredClientRepository registeredClientRepository, ApplicationEventPublisher eventPublisher
                                          ) {
        this.redisTemplate = redisTemplate;
        this.registeredClientRepository = registeredClientRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        if (authorization.getAuthorizationGrantType() == AuthorizationGrantType.CLIENT_CREDENTIALS) {
            return;
        }
        Objects.requireNonNull(authorization, "authorization cannot be null");
        var jti = Objects.requireNonNull(authorization.getAccessToken().getClaims()).get(TokenUtils.JWT_CLAIM_JTI).toString();
        var userId = Objects.requireNonNull(authorization.getAccessToken().getClaims()).get(TokenUtils.JWT_CLAIM_USER_NAME).toString();
        //验证token存储策略并移除
        validJwtActiveStoreAndRemove(userId);
        var id = authorization.getId();
        //添加访问令牌映射
        var accessToken = authorization.getAccessToken().getToken();
        //保存授权主体
        redisTemplate.opsForValue().set(
                AUTHORIZATION_KEY.replace("{id}", id),
                ModelMapper.convertOAuth2AuthorizationGrantAuthorization(authorization),
                calculateTtl(authorization),
                TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(
                ACCESS_TOKEN_TO_AUTHORIZATION.replace("{token}", accessToken.getTokenValue()),
                authorization.getId(),
                Duration.between(Instant.now(), accessToken.getExpiresAt()).getSeconds(),
                TimeUnit.SECONDS);
        //添加刷新令牌映射（如果存在）
        Optional.ofNullable(authorization.getRefreshToken())
                .map(OAuth2Authorization.Token::getToken).ifPresent(refreshToken -> redisTemplate.opsForValue().set(
                        REFRESH_TOKEN_TO_AUTHORIZATION.replace("{token}", refreshToken.getTokenValue()),
                        authorization.getId(),
                        Duration.between(Instant.now(), refreshToken.getExpiresAt()).getSeconds(),
                        TimeUnit.SECONDS));
        var userTokenKey = getUserTokenKey(userId);
        var userTokenHKey = getUserTokenHashKey(jti);
        redisTemplate.opsForHash()
                .put(userTokenKey, userTokenHKey, authorization.getAccessToken().getToken().getTokenValue());
        redisTemplate.opsForHash()
                .expire(userTokenKey, Duration.between(Instant.now(), accessToken.getExpiresAt()), Collections.singleton(userTokenHKey));
    }

    private void validJwtActiveStoreAndRemove(String userId) {
        var config = RedisJwtActiveStoreProperties.getConfig();
        if (config.getMultiSiteLoginValid()) {
            var userTokenKey = getUserTokenKey(userId);
            var userTokenHKeys = this.redisTemplate.opsForHash().keys(userTokenKey);
            if (CollectionUtils.isEmpty(userTokenHKeys)) {
                return;
            }
            var terminal = SessionContextHolder.getRequester().getTerminal();
            var userTokenTerminalKeys = userTokenHKeys.stream().filter(key -> key.toString().contains(terminal))
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(userTokenTerminalKeys)) {
                return;
            }
            //发送token踢出事件
            eventPublisher.publishEvent(new TokenRemoveEvent(this, userId, SessionContextHolder.getRequester()
                    .getTerminal(), userTokenTerminalKeys));
            for (var key : userTokenTerminalKeys) {
                var token = (String) this.redisTemplate.opsForHash().get(userTokenKey, key);
                var auth2Authorization = findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
                if (Objects.nonNull(auth2Authorization)) {
                    this.remove(auth2Authorization);
                }
            }
        }
    }

    private String getUserTokenKey(String userId) {
        return USER_ACCESS_TOKEN_TO_AUTHORIZATION.replace("{userJti}", userId);
    }

    private String getUserTokenHashKey(String jti) {
        var terminal = SessionContextHolder.getRequester().getTerminal();
        return terminal + ":" + jti;
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Objects.requireNonNull(authorization, "authorization cannot be null");
        var id = authorization.getId();
        //删除授权主体
        redisTemplate.delete(AUTHORIZATION_KEY.replace("{id}", id));
        //删除访问令牌映射
        var accessToken = authorization.getAccessToken().getToken();
        redisTemplate.delete(ACCESS_TOKEN_TO_AUTHORIZATION.replace("{token}", accessToken.getTokenValue()));
        //删除刷新令牌映射（如果存在）
        Optional.ofNullable(authorization.getRefreshToken())
                .map(OAuth2Authorization.Token::getToken).ifPresent(refreshToken -> redisTemplate.delete(
                        REFRESH_TOKEN_TO_AUTHORIZATION.replace("{token}", refreshToken.getTokenValue())));
        if (authorization.getAuthorizationGrantType() != AuthorizationGrantType.CLIENT_CREDENTIALS) {
            var userId = Objects.requireNonNull(authorization.getAccessToken().getClaims()).get(TokenUtils.JWT_CLAIM_USER_NAME).toString();
            var jti = Objects.requireNonNull(authorization.getAccessToken().getClaims()).get(TokenUtils.JWT_CLAIM_JTI).toString();
            //删除用户令牌映射
            redisTemplate.opsForHash().delete(getUserTokenKey(userId), getUserTokenHashKey(jti));
        }
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Objects.requireNonNull(id, "id cannot be null");
        var authorizationObj = redisTemplate.opsForValue().get(AUTHORIZATION_KEY.replace("{id}", id));
        if (Objects.isNull(authorizationObj)) {
            return null;
        }
        var grantAuthorization = (OAuth2AuthorizationGrantAuthorization) authorizationObj;
        var registeredClient = registeredClientRepository.findByClientId(grantAuthorization.getRegisteredClientId());
        var builder = OAuth2Authorization.withRegisteredClient(Objects.requireNonNull(registeredClient));
        ModelMapper.mapOAuth2AuthorizationGrantAuthorization(grantAuthorization, registeredClient, builder);
        return builder.build();
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Objects.requireNonNull(token, "token cannot be null");
        String key;
        if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            key = ACCESS_TOKEN_TO_AUTHORIZATION.replace("{token}", token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            key = REFRESH_TOKEN_TO_AUTHORIZATION.replace("{token}", token);
        } else {
            // 不支持其他令牌类型
            return null;
        }
        var authorizationId = (String) redisTemplate.opsForValue().get(key);
        if (authorizationId == null) {
            return null;
        }
        return findById(authorizationId);
    }

    /**
     * 计算Redis存储的TTL（基于令牌过期时间）
     *
     * @param authorization
     *
     * @return
     */
    private long calculateTtl(OAuth2Authorization authorization) {
        var maxExpiry = authorization.getAccessToken().getToken().getExpiresAt();
        if (Objects.isNull(maxExpiry)) {
            return 0L;
        }
        if (authorization.getRefreshToken() != null) {
            var refreshExpiry = authorization.getRefreshToken().getToken().getExpiresAt();
            if (refreshExpiry != null && refreshExpiry.isAfter(maxExpiry)) {
                maxExpiry = refreshExpiry;
            }
        }
        // 添加安全缓冲时间（10分钟）
        return Duration.between(Instant.now(), maxExpiry.plus(Duration.ofMinutes(10)))
                .getSeconds();
    }
}