package com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.service;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.BroadcastService;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.facade.event.LiuZhouPlayTextEvent;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.facade.request.LiuZhouPlayTextRequest;
import com.guhecloud.cube.biz.adapter.broadcast.server.exception.BroadcastPlayFailException;
import com.guhecloud.cube.biz.adapter.broadcast.server.param.DevicePlayTextParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiuZhouBroadcastService implements BroadcastService {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String devicePlayText(DevicePlayTextParam playTextParam) {
        LiuZhouPlayTextRequest liuZhouPlayTextRequest = new LiuZhouPlayTextRequest();
        liuZhouPlayTextRequest.setDevice_ids(playTextParam.getDeviceIds());
        liuZhouPlayTextRequest.setMessage(playTextParam.getPlayContent());
        LiuZhouPlayTextEvent liuZhouPlayTextEvent = new LiuZhouPlayTextEvent(this, liuZhouPlayTextRequest);
        this.applicationEventPublisher.publishEvent(liuZhouPlayTextEvent);
        if (!"200".equals(liuZhouPlayTextEvent.get())) {
            throw new BroadcastPlayFailException("广播喊话执行失败");
        }
        return liuZhouPlayTextEvent.get();
    }

    @Override
    public Integer devicePlayTextState(String playTextId) {
        throw new UnsupportedOperationException("liu zhou broadcast not impl");
    }
}
