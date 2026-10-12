package com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.service;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.BroadcastService;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.facade.event.TianJinPlayTextEvent;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.facade.request.TianJinPlayTextRequest;
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
public class TianJinBroadcastService implements BroadcastService {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String devicePlayText(DevicePlayTextParam playTextParam) {
        TianJinPlayTextRequest tianJinPlayTextRequest = new TianJinPlayTextRequest();
        tianJinPlayTextRequest.setDeviceIds(playTextParam.getDeviceIds());
        tianJinPlayTextRequest.setPlayTextContent(playTextParam.getPlayContent());
        TianJinPlayTextEvent tianJinPlayTextEvent = new TianJinPlayTextEvent(this, tianJinPlayTextRequest);
        this.applicationEventPublisher.publishEvent(tianJinPlayTextEvent);
        if (!"0".equals(tianJinPlayTextEvent.get())) {
            throw new BroadcastPlayFailException("广播喊话执行失败");
        }
        return tianJinPlayTextEvent.get();
    }

    @Override
    public Integer devicePlayTextState(String playTextId) {
        throw new UnsupportedOperationException("tian jin broadcast not impl");
    }
}
