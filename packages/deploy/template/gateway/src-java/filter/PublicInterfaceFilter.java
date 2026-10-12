package com.guhecloud.cube.gateway.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.gateway.utils.JsonUtil;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/8 16:07
 * @Description:
 */

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PublicInterfaceFilter implements WebFilter {

    private static final String AUTHORIZATION_KEY = "PUBLIC:INTERFACE:AUTHORIZATION";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private JsonUtil jsonUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        var request = exchange.getRequest();
        var headers = request.getHeaders().toSingleValueMap();
        var authorizationFlag = false;
        var tenant = "0";
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (StringUtils.equalsIgnoreCase(entry.getKey(), HttpHeaders.AUTHORIZATION)) {
                authorizationFlag = true;
            }
            if (StringUtils.equalsIgnoreCase(entry.getKey(), "tenant")) {
                tenant = entry.getValue();
            }
        }
        if (!authorizationFlag && checkUrlPublic(tenant, request.getURI().getPath())) {
            var serverHttpRequest = exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, getClientAuthorization())
                    .build();
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        }
        return chain.filter(exchange);
    }

    public boolean checkUrlPublic(String tenant, String url) {
        Set<String> keys = this.redisTemplate.keys("DICTIONARY:PUBLIC_URL:" + tenant + "*");

        if (CollectionUtils.isEmpty(keys)) {
            return false;
        }
        List<Object> objectList = redisTemplate.opsForValue().multiGet(keys);
        if (CollectionUtils.isEmpty(objectList)) {
            return false;
        }
        List<Map<String, String>> mapList = jsonUtil.deserialize(objectList.toString(), new TypeReference<List<Map<String, String>>>() {
        });
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (Map<String, String> object : mapList) {
            if (StringUtils.isNotBlank(object.get("variable")) &&
                antPathMatcher.match(object.get("variable"), url)) {
                return true;
            }
        }
        return false;
    }

    private String getClientAuthorization() {

        Object authorizationCache = redisTemplate.opsForValue().get(AUTHORIZATION_KEY);
        if (Objects.nonNull(authorizationCache)) {
            return authorizationCache.toString();
        }
        return null;
    }

}