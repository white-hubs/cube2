package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampEventSubscribeService;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务启后执行
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EventBusListen(order = 1)
public class LampMyApplicationRunnerImpl {

    private final LampEventSubscribeService lampEventSubscribeService;

    /**
     * Callback used to run the bean.
     *
     * @param event System Read Event
     */
    @Subscribe
    @AllowConcurrentEvents
    public void loadSubLamp(SysReadEvent event) {
        // 订阅访客业务事件
        lampEventSubscribeService.subscribeVisitorBusinessEvent();
    }
}
