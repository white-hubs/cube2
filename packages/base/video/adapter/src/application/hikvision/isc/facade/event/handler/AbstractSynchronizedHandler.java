package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.BasicResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSynchronizedHandler<E, R> {
    /**
     * 构建请求
     */
    public abstract R buildRequest(E t);

    /**
     * 发送请求
     */
    public abstract BasicResponse requesting(R r);

    /**
     * 获取结果
     */

    public <RES> RES handle(E t) {
        log.info("event info is {}", t);
        R r = buildRequest(t);
        BasicResponse response = requesting(r);
        // 结果处理
        log.info("request success,this response is {}", response);
        return (RES) response;
    }
}
