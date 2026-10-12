package com.guhecloud.cube.biz.adapter.park.server.application.oasis.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.biz.adapter.park.server.application.oasis.config.OasisConfig;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class OasisClient {
    private static final String TOKEN_HEADER = "X-H3C-TOKEN";
    private static final String TOKEN_PATH = "/api/token/getToken";
    private static final String[] TOKEN_FIELDS = {"token", "accessToken", "access_token", "X-H3C-TOKEN"};

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Map<String, TokenEntry> tokenCache = new ConcurrentHashMap<>();

    public JsonNode get(OasisConfig config, int tokenCode, String path, Map<String, ?> query) {
        String cacheKey = config.getTokenBaseUrl() + ":" + config.getAppId() + ":" + tokenCode;
        try {
            return request(config, tokenCode, path, query, cacheKey);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() != HttpStatus.UNAUTHORIZED
                    && exception.getStatusCode() != HttpStatus.FORBIDDEN) {
                throw exception;
            }
            tokenCache.remove(cacheKey);
            return request(config, tokenCode, path, query, cacheKey);
        }
    }

    private JsonNode request(OasisConfig config, int tokenCode, String path, Map<String, ?> query, String cacheKey) {
        String token = token(config, tokenCode, cacheKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set(TOKEN_HEADER, token);
        URI uri = buildUri(config.getApiBaseUrl(), path, query);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        JsonNode body = parse(response.getBody(), "绿洲停车业务接口响应解析失败");
        if (unauthorized(body)) {
            tokenCache.remove(cacheKey);
            token = token(config, tokenCode, cacheKey);
            headers.set(TOKEN_HEADER, token);
            response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            body = parse(response.getBody(), "绿洲停车业务接口响应解析失败");
        }
        assertSuccess(body);
        return body;
    }

    private String token(OasisConfig config, int tokenCode, String cacheKey) {
        TokenEntry cached = tokenCache.get(cacheKey);
        if (cached != null && cached.expiresAt().isAfter(Instant.now())) {
            return cached.value();
        }
        URI uri = UriComponentsBuilder.fromUriString(trim(config.getTokenBaseUrl()) + TOKEN_PATH)
                .queryParam("appId", config.getAppId())
                .queryParam("code", tokenCode)
                .build(true)
                .toUri();
        String response = restTemplate.getForObject(uri, String.class);
        JsonNode body = parse(response, "绿洲停车Token响应解析失败");
        if (body.has("code")) {
            assertSuccess(body);
        }
        String token = extractToken(body);
        if (StringUtils.isBlank(token)) {
            throw new ServiceException("绿洲停车Token响应中未找到token");
        }
        Instant expiresAt = tokenExpiresAt(body, config);
        tokenCache.put(cacheKey, new TokenEntry(token, expiresAt));
        return token;
    }

    private static Instant tokenExpiresAt(JsonNode body, OasisConfig config) {
        JsonNode expireNode = body.path("data").path("expire");
        if (expireNode.canConvertToLong()) {
            long expire = expireNode.asLong();
            Instant expiresAt = expire > 10_000_000_000L
                    ? Instant.ofEpochMilli(expire)
                    : Instant.ofEpochSecond(expire);
            Instant safeExpiresAt = expiresAt.minusSeconds(30);
            if (safeExpiresAt.isAfter(Instant.now())) {
                return safeExpiresAt;
            }
        }
        long ttl = config.getTokenTtlSeconds() == null ? 300L : Math.max(config.getTokenTtlSeconds(), 1L);
        return Instant.now().plusSeconds(ttl);
    }

    private String extractToken(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isTextual()) {
            return node.asText();
        }
        if (node.isArray()) {
            for (JsonNode child : node) {
                String token = extractToken(child);
                if (StringUtils.isNotBlank(token)) {
                    return token;
                }
            }
            return null;
        }
        for (String field : TOKEN_FIELDS) {
            JsonNode value = node.get(field);
            if (value != null && value.isValueNode() && StringUtils.isNotBlank(value.asText())) {
                return value.asText();
            }
        }
        JsonNode data = node.get("data");
        if (data != null) {
            String token = extractToken(data);
            if (StringUtils.isNotBlank(token)) {
                return token;
            }
        }
        if (node.isObject()) {
            Iterator<JsonNode> values = node.elements();
            while (values.hasNext()) {
                JsonNode child = values.next();
                if (child.isObject()) {
                    String token = extractToken(child);
                    if (StringUtils.isNotBlank(token)) {
                        return token;
                    }
                }
            }
        }
        return null;
    }

    private static URI buildUri(String baseUrl, String path, Map<String, ?> query) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(trim(baseUrl) + "/" + path.replaceFirst("^/+", ""));
        query.forEach((key, value) -> {
            if (value != null && (!(value instanceof String text) || StringUtils.isNotBlank(text))) {
                builder.queryParam(key, value);
            }
        });
        return builder.build().encode().toUri();
    }

    private JsonNode parse(String value, String errorMessage) {
        if (StringUtils.isBlank(value)) {
            throw new ServiceException(errorMessage + "：响应为空");
        }
        try {
            return objectMapper.readTree(value);
        } catch (JsonProcessingException exception) {
            log.error("{}", errorMessage, exception);
            throw new ServiceException(errorMessage);
        }
    }

    private static boolean unauthorized(JsonNode body) {
        String code = body.path("code").asText();
        return "401".equals(code) || "403".equals(code);
    }

    private static void assertSuccess(JsonNode body) {
        String code = body.path("code").asText();
        if (!"0".equals(code) && !"200".equals(code)) {
            throw new ServiceException("绿洲停车接口请求失败：" + body.path("msg").asText(code));
        }
    }

    private static String trim(String baseUrl) {
        return baseUrl.replaceFirst("/+$", "");
    }

    private record TokenEntry(String value, Instant expiresAt) { }
}
