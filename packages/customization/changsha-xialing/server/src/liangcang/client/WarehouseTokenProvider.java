package com.guhecloud.cube.customization.xialing.server.liangcang.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.JsonBody;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.JsonKeyCommon;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgAuth;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.Redis;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.TenantDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.ThirdPartyApi;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseLoginCredentialDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** 智慧仓库 baseUrl、x-token；字典账号 + Redis 缓存 + 分布式锁下登录。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseTokenProvider {
    private static final long TOKEN_LOCK_WAIT_SECONDS = 10L;
    private static final long TOKEN_LOCK_LEASE_SECONDS = 30L;
    private static final long LOGIN_TOKEN_CACHE_SECONDS = 600L;

    private final RestTemplate restTemplate;
    private final DataDictionaryClient dataDictionaryClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;

    public String getBaseUrl() {
        WarehouseLoginCredentialDTO credential = loadCredentialFromTenantDictionary();
        String base = credential.getBaseUrl();
        Assert.isParameterError(!(StringUtils.hasText(base)), MsgAuth.TENANT_DICT_BASE_URL_EMPTY);
        return trimTrailingSlash(base);
    }

    public String getToken() {
        String redisKey = redisTokenKey();
        String fromRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.hasText(fromRedis)) {
            return fromRedis.trim();
        }
        String lockKey = Redis.LOCK_PREFIX + redisKey;
        RLock rLock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = rLock.tryLock(TOKEN_LOCK_WAIT_SECONDS, TOKEN_LOCK_LEASE_SECONDS, TimeUnit.SECONDS);
            if (!locked) {
                fromRedis = stringRedisTemplate.opsForValue().get(redisKey);
                if (StringUtils.hasText(fromRedis)) {
                    return fromRedis.trim();
                }
            }
            Assert.isParameterError(!(locked), MsgAuth.WAREHOUSE_TOKEN_BUSY);
            fromRedis = stringRedisTemplate.opsForValue().get(redisKey);
            if (StringUtils.hasText(fromRedis)) {
                return fromRedis.trim();
            }
            String baseUrl = getBaseUrl();
            try {
                String token = loginAndGetToken(baseUrl);
                stringRedisTemplate.opsForValue()
                        .set(redisKey, token, Math.max(60L, LOGIN_TOKEN_CACHE_SECONDS), TimeUnit.SECONDS);
                return token;
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                ServiceException tokenFail = new ServiceException(MsgAuth.WAREHOUSE_TOKEN_FAILED);
                tokenFail.initCause(e);
                throw tokenFail;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ServiceException tokenInterrupted = new ServiceException(MsgAuth.WAREHOUSE_TOKEN_INTERRUPTED);
            tokenInterrupted.initCause(e);
            throw tokenInterrupted;
        } finally {
            if (locked && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    private String redisTokenKey() {
        Integer tenant = currentTenant();
        return Redis.TOKEN_KEY_PREFIX + (tenant == null ? Redis.TENANT_PLACEHOLDER_NONE : tenant);
    }

    private String loginAndGetToken(String baseUrl) {
        WarehouseLoginCredentialDTO credential = loadCredentialFromTenantDictionary();
        String account = credential.getAccount();
        Assert.isParameterError(!(StringUtils.hasText(account) && StringUtils.hasText(credential.getPassword())), MsgAuth.TENANT_ACCOUNT_PASSWORD_EMPTY);

        Map<String, Object> body = new HashMap<>();
        body.put(JsonBody.ACCOUNT, account);
        body.put(JsonBody.PASSWORD, credential.getPassword());

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.ALL));

        String url = baseUrl + ThirdPartyApi.PATH_USER_LOGIN;
        Map<String, Object> logBody = new HashMap<>(body);
        logBody.put(JsonBody.PASSWORD, "******");
        log.info("三方接口请求 api=userLogin method=POST url={} body={}", url, logBody);
        var resp = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
        log.info("三方接口响应 api=userLogin status={} body={}", resp.getStatusCode(), maskSensitiveBody(resp.getBody()));
        Assert.isParameterError(!(resp.getStatusCode().is2xxSuccessful()), MsgAuth.USER_LOGIN_NOT_SUCCESS_PREFIX + resp.getStatusCode());
        Assert.isParameterError(resp.getBody() == null, MsgAuth.USER_LOGIN_NOT_SUCCESS_PREFIX + resp.getStatusCode());
        String token = parseToken(resp.getBody());
        Assert.isParameterError(!(StringUtils.hasText(token)), MsgAuth.USER_LOGIN_NO_TOKEN);
        return token;
    }

    private WarehouseLoginCredentialDTO loadCredentialFromTenantDictionary() {
        Assert.isParameterError(!(StringUtils.hasText(TenantDictionary.ITEM_THIRD_CONFIG)), MsgAuth.DICT_ITEM_CODE_NOT_CONFIGURED);

        String variable = null;
        if (StringUtils.hasText(TenantDictionary.CODE_WAREHOUSE_LOGIN)) {
            var p = new DataDictionaryaramByItemAndCodeParam();
            p.setItemCode(TenantDictionary.ITEM_THIRD_CONFIG);
            p.setCode(TenantDictionary.CODE_WAREHOUSE_LOGIN);
            var dto = dataDictionaryClient.getDataDictionaryByItemAndCode(p);
            if (dto != null) {
                variable = dto.getVariable();
            }
        }
        if (!StringUtils.hasText(variable)) {
            var p = new DataDictionaryaramParam();
            p.setItemCode(TenantDictionary.ITEM_THIRD_CONFIG);
            List<DataDictionaryDTO> list = dataDictionaryClient.getDataDictionaryByItem(p);
            if (ListUtils.nonEmpty(list)) {
                variable = list.get(0).getVariable();
            }
        }
        Assert.isParameterError(!(StringUtils.hasText(variable)), MsgAuth.TENANT_DICT_WAREHOUSE_LOGIN_MISSING);
        try {
            return JsonUtils.deserialize(variable, WarehouseLoginCredentialDTO.class);
        } catch (RuntimeException e) {
            ServiceException varParse = new ServiceException(MsgAuth.TENANT_DICT_VARIABLE_PARSE_FAILED);
            varParse.initCause(e);
            throw varParse;
        }
    }

    private String parseToken(String body) {
        JsonNode root = JsonUtils.deserialize(body);
        if (root == null) {
            return null;
        }
        String token = text(root, JsonKeyCommon.TOKEN);
        if (StringUtils.hasText(token)) {
            return token;
        }
        JsonNode data = root.get(JsonKeyCommon.DATA);
        token = text(data, JsonKeyCommon.TOKEN);
        if (StringUtils.hasText(token)) {
            return token;
        }
        JsonNode result = root.get(JsonKeyCommon.RESULT);
        return text(result, JsonKeyCommon.TOKEN);
    }

    private static String text(JsonNode node, String field) {
        if (node == null || !node.has(field) || node.get(field).isNull()) {
            return null;
        }
        return node.get(field).asText();
    }

    private static String maskSensitiveBody(String body) {
        if (body == null) {
            return null;
        }
        return body.replaceAll("(?i)(\"token\"\\s*:\\s*\")[^\"]*(\")", "$1******$2")
                .replaceAll("(?i)(\"password\"\\s*:\\s*\")[^\"]*(\")", "$1******$2");
    }

    private static String trimTrailingSlash(String base) {
        if (base.endsWith("/")) {
            return base.substring(0, base.length() - 1);
        }
        return base;
    }

    private static Integer currentTenant() {
        Requester requester = SessionContextHolder.getRequester(true);
        return requester == null ? null : requester.getTenant();
    }
}
