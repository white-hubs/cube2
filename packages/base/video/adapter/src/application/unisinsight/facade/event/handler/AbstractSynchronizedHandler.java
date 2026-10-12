package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event.handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSynchronizedHandler<E, R> {

    public abstract R buildRequest(E t);

    public abstract Object requesting(R r);

    public <RES> RES handle(E t) {
        log.info("event info is {}", t);
        R r = buildRequest(t);
        Object response = requesting(r);
        log.info("request success,this response is {}", response);
        return (RES) response;
    }
}
