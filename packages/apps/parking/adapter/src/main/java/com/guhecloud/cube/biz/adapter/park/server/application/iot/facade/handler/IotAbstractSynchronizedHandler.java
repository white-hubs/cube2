package com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.handler;

import com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.response.IotBasicResponse;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
public abstract class IotAbstractSynchronizedHandler<E, R> {
    public RestTemplate requester() {
        return ApplicationContextHolder.getBean(RestTemplate.class);
    }

    /**
     * 构建请求
     */
    public abstract R buildRequest(E t);

    /**
     * 发送请求
     */
    public abstract IotBasicResponse requesting(R r);

    /**
     * 获取结果
     */

    public <RES> RES handle(E t) {
        log.info("event info is {}", t);
        R r = buildRequest(t);
        IotBasicResponse response = requesting(r);
        // 结果处理
        log.info("request success,this response is {}", response);
        return (RES) response;
    }
}
