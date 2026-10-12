package com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.facade.event.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.http.HttpClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.security.GeneralSecurityException;

@Slf4j
public abstract class AbstractLiuZhouSynchronizedHandler<E, R> {

    private static RestTemplate REST_TEMPLATE;

    private static RestTemplate getRedisTemplate() {
        try {
            if (REST_TEMPLATE == null) {
                REST_TEMPLATE = HttpClientFactory.createRestTemplate(ApplicationContextHolder.getBean(ObjectMapper.class));
            }
            return REST_TEMPLATE;
        } catch (GeneralSecurityException e) {
            log.error("create rest template error.", e);
        }
        return null;
    }

    public RestTemplate requester() {
        return AbstractLiuZhouSynchronizedHandler.getRedisTemplate();
    }

    /**
     * 构建请求
     */
    public abstract R buildRequest(E t);

    /**
     * 发送请求
     */
    public abstract String requesting(R r);

    /**
     * 获取结果
     */

    public <P> P handle(E t) {
        log.info("event info is {}", t);
        R r = buildRequest(t);
        String response = requesting(r);
        // 结果处理
        log.info("request success,this response is {}", response);
        return (P) response;
    }
}
