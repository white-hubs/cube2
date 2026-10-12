package com.guhecloud.cube.customization.xialing.server.drone.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.customization.xialing.server.drone.dto.XianingLoginCredentialDTO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 霞凝无人机SSO Token提供者。
 * <p>
 * 供可视化 SSO 链接生成使用。
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XianingSsoTokenProvider {
    private static final String TOKEN_PATH = "/gdu-unified-portal-api/auth/oauth2/token";
    private static final String LOGIN_AUTHORIZATION = "Basic Z2R1LXVhcC11cG1zOmdkdS11YXAtdXBtcw==";
    private static final String REDIS_TOKEN_KEY_PREFIX = "subsystem:drone:token:sso:";
    private static final String FRESH_REDIS_TOKEN_KEY_PREFIX = "subsystem:drone:token:sso:fresh:";
    private static final String TOKEN_REFRESH_LOCK_PREFIX = "lock:";
    private static final long TOKEN_LOCK_WAIT_SECONDS = 10L;
    private static final long TOKEN_LOCK_LEASE_SECONDS = 30L;
    private static final long LOGIN_TOKEN_CACHE_SECONDS = 43_000L;
    private static final long FRESH_TOKEN_CACHE_SECONDS = 5L;

    private final RestTemplate restTemplate;
    private final DroneThirdPartyConfigProvider configProvider;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;

    public String getBaseUrl() {
        XianingLoginCredentialDTO credential = configProvider.currentCredential();
        Assert.isParameterError(!(StringUtils.isNotBlank(credential.getBaseUrl())), "霞凝无人机 baseUrl 为空");
        return trimTrailingSlash(credential.getBaseUrl());
    }

    public String getTenantId() {
        var config = configProvider.currentConfig();
        XianingLoginCredentialDTO credential = config.credential();
        if (StringUtils.isNotBlank(credential.getTenantId())) {
            return credential.getTenantId().trim();
        }
        return String.valueOf(config.tenant());
    }

    public String getClientId() {
        XianingLoginCredentialDTO credential = configProvider.currentConfig().credential();
        if (StringUtils.isNotBlank(credential.getClientId())) {
            return credential.getClientId().trim();
        }
        return "nest";
    }

    public String getToken() {
        String redisKey = redisTokenKey();
        String fromRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(fromRedis)) {
            return fromRedis.trim();
        }
        String lockKey = TOKEN_REFRESH_LOCK_PREFIX + redisKey;
        RLock rLock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = rLock.tryLock(TOKEN_LOCK_WAIT_SECONDS, TOKEN_LOCK_LEASE_SECONDS, TimeUnit.SECONDS);
            if (!locked) {
                fromRedis = stringRedisTemplate.opsForValue().get(redisKey);
                if (StringUtils.isNotBlank(fromRedis)) {
                    return fromRedis.trim();
                }
                Assert.isParameterError(true, "获取霞凝无人机SSO token 繁忙，请稍后重试");
                return "";
            }
            fromRedis = stringRedisTemplate.opsForValue().get(redisKey);
            if (StringUtils.isNotBlank(fromRedis)) {
                return fromRedis.trim();
            }
            String token = loginAndGetToken();
            stringRedisTemplate.opsForValue().set(redisKey, token, LOGIN_TOKEN_CACHE_SECONDS, TimeUnit.SECONDS);
            return token;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.isParameterError(true, "获取霞凝无人机SSO token 被中断");
            return "";
        } finally {
            if (locked && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    public String getFreshToken() {
        String redisKey = freshRedisTokenKey();
        String fromRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(fromRedis)) {
            return fromRedis.trim();
        }
        String lockKey = TOKEN_REFRESH_LOCK_PREFIX + redisKey;
        RLock rLock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = rLock.tryLock(TOKEN_LOCK_WAIT_SECONDS, TOKEN_LOCK_LEASE_SECONDS, TimeUnit.SECONDS);
            if (!locked) {
                fromRedis = stringRedisTemplate.opsForValue().get(redisKey);
                if (StringUtils.isNotBlank(fromRedis)) {
                    return fromRedis.trim();
                }
                Assert.isParameterError(true, "获取霞凝无人机SSO token 繁忙，请稍后重试");
                return "";
            }
            fromRedis = stringRedisTemplate.opsForValue().get(redisKey);
            if (StringUtils.isNotBlank(fromRedis)) {
                return fromRedis.trim();
            }
            String token = loginAndGetToken();
            stringRedisTemplate.delete(redisTokenKey());
            stringRedisTemplate.opsForValue().set(redisKey, token, FRESH_TOKEN_CACHE_SECONDS, TimeUnit.SECONDS);
            return token;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.isParameterError(true, "获取霞凝无人机SSO token 被中断");
            return "";
        } finally {
            if (locked && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    public void evictToken() {
        stringRedisTemplate.delete(redisTokenKey());
        stringRedisTemplate.delete(freshRedisTokenKey());
    }

    private String redisTokenKey() {
        var config = configProvider.currentConfig();
        return REDIS_TOKEN_KEY_PREFIX + config.tenant() + ":" + config.manufacturer();
    }

    private String freshRedisTokenKey() {
        var config = configProvider.currentConfig();
        return FRESH_REDIS_TOKEN_KEY_PREFIX + config.tenant() + ":" + config.manufacturer();
    }

    private String loginAndGetToken() {
        XianingLoginCredentialDTO credential = configProvider.currentConfig().credential();
        Assert.isParameterError(StringUtils.isBlank(credential.getSsoUsername()) || StringUtils.isBlank(credential.getSsoPassword()),
                                "霞凝无人机SSO账号或密码为空");

        String url = UriComponentsBuilder.fromUriString(getBaseUrl() + TOKEN_PATH)
                .queryParam("grant_type", "password")
                .queryParam("username", credential.getSsoUsername().trim())
                .queryParam("password", credential.getSsoPassword().trim())
                .queryParam("scope", "server")
                .build(true)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", LOGIN_AUTHORIZATION);
        headers.set("TENANT-ID", getTenantId());

        String body = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(null, headers), String.class).getBody();
        Assert.isParameterError(!(StringUtils.isNotBlank(body)), "霞凝无人机SSO登录响应为空");
        JsonNode root = JsonUtils.deserialize(body);
        Assert.isParameterError(root == null, "霞凝无人机SSO登录响应无法解析");
        JsonNode codeNode = root.get("code");
        Assert.isParameterError(!(codeNode != null && codeNode.asInt() == 0), "霞凝无人机SSO登录失败: " + root.path("msg").asText());
        JsonNode tokenNode = root.path("data").path("access_token");
        Assert.isParameterError(tokenNode.isMissingNode() || StringUtils.isBlank(tokenNode.asText()), "霞凝无人机SSO登录未返回 access_token");
        return tokenNode.asText().trim();
    }

    private String trimTrailingSlash(String value) {
        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }
}
