package com.guhecloud.cube.gateway.config;

import com.guhecloud.cube.gateway.listener.PublicInterfaceTokenExpirationThread;
import com.guhecloud.cube.gateway.utils.JsonUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/11 13:41
 * @Description:
 */

@Slf4j
@Component
public class PublicInterfaceTokenExpirationConfig implements InitializingBean, DisposableBean {
    private PublicInterfaceTokenExpirationThread expirationThread = null;
    @Value("${spring.security.oauth2.resourceclient.clientcredential.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceclient.clientcredential.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.resourceclient.clientcredential.access-token-uri}")
    private String tokenUri;
    @Getter
    private final RedisTemplate<String, Object> redisTemplate;
    @Getter
    private final JsonUtil jsonUtil;

    public PublicInterfaceTokenExpirationConfig(RedisTemplate<String, Object> redisTemplate, JsonUtil jsonUtil) {
        this.redisTemplate = redisTemplate;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public void afterPropertiesSet() {
        expirationThread = new PublicInterfaceTokenExpirationThread(redisTemplate, jsonUtil, clientId, clientSecret, tokenUri);
        expirationThread.setDaemon(true);
        expirationThread.setName("publicInterfaceTokenExpirationThread");
        expirationThread.start();
    }

    @Override
    public void destroy() {
        if (Objects.nonNull(expirationThread)) {
            expirationThread.toStop();
            stopThread(expirationThread);
        }
    }

    private void stopThread(Thread thread) {
        try {
            TimeUnit.SECONDS.sleep(1);  // wait
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        if (thread.getState() != Thread.State.TERMINATED) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
