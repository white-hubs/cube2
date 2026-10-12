package com.guhecloud.cube.gateway.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.gateway.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/11 11:42
 * @Description: public接口token过期监听线程
 */
@Slf4j
public class PublicInterfaceTokenExpirationThread extends Thread {
    private volatile boolean toStop = false;
    private static final String AUTHORIZATION_KEY = "PUBLIC:INTERFACE:AUTHORIZATION";
    private final String clientId;
    private final String clientSecret;
    private final String tokenUri;

    private final RedisTemplate<String, Object> redisTemplate;

    private final JsonUtil jsonUtil;

    public PublicInterfaceTokenExpirationThread(RedisTemplate<String, Object> redisTemplate, JsonUtil jsonUtil,
                                                String clientId, String clientSecret, String tokenUri
                                               ) {
        this.redisTemplate = redisTemplate;
        this.jsonUtil = jsonUtil;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUri = tokenUri;
    }

    @Override
    public void run() {
        while (!toStop) {
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                if (!toStop) {
                    log.error(e.getMessage(), e);
                }
            }
            try {
                Long expire = redisTemplate.getExpire(AUTHORIZATION_KEY, TimeUnit.MINUTES);
                Object authorizationCache = redisTemplate.opsForValue().get(AUTHORIZATION_KEY);
                //如果缓存中存在，并且过期时间大于5分钟，则不进行刷新
                if (Objects.nonNull(authorizationCache) && Objects.nonNull(expire) && expire > 5) {
                    continue;
                }
                HttpClient.create().post()
                        .uri(tokenUri)
                        .sendForm((req, out) -> {
                            out.attr("grant_type", "client_credentials");
                            out.attr("client_id", clientId);
                            out.attr("client_secret", clientSecret);
                        })
                        .responseSingle(this::doResponse)
                        .subscribe(this::refreshCache);
            } catch (Exception e) {
                if (!toStop) {
                    log.error("get public token error", e);
                }
            }
        }
    }

    /**
     * 刷新缓存
     *
     * @param response
     */
    private void refreshCache(Map<String, Object> response) {
        String authorization = response.get("token_type") + " " + response.get("access_token");
        redisTemplate.opsForValue().set(AUTHORIZATION_KEY, authorization, 30, TimeUnit.MINUTES);
    }

    /**
     * 响应结果处理
     *
     * @param response
     * @param byteBufMono
     *
     * @return
     */
    private Mono<Map<String, Object>> doResponse(HttpClientResponse response, ByteBufMono byteBufMono) {
        // 判断状态码
        if (response.status().code() == 200) {
            // 如果状态码是200，我们可以尝试读取响应体
            return byteBufMono.asString() // 将ByteBuf转换为String
                    .map(responseBody -> jsonUtil.deserialize(responseBody, new TypeReference<Map<String, Object>>() {
                    }));
        } else {
            // 如果状态码不是200，我们可以返回一个错误消息或抛出一个异常
            return Mono.error(new RuntimeException("Failed : HTTP error code : " + response.status().code()));
        }
    }

    public void toStop() {
        this.toStop = true;
    }
}
