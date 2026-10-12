package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.event.handler;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.event.BroadcastPlayTextEvent;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.BroadcastPlayTextResponse;
import com.guhecloud.subsystem.requester.sdk.client.BroadcastClient;
import com.guhecloud.subsystem.requester.sdk.param.broadcast.DevicePlayTextParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastPlayTextEventHandler {

    private final BroadcastClient broadcastClient;

    @EventListener
    public void broadcastPlayText(final BroadcastPlayTextEvent broadcastPlayTextEvent) {
        BroadcastPlayTextResponse broadcastPlayTextResponse = new BroadcastPlayTextResponse();
        try {
            DevicePlayTextParam devicePlayTextParam = new DevicePlayTextParam();
            devicePlayTextParam.setPlayContent(broadcastPlayTextEvent.getContent());
            devicePlayTextParam.setDeviceIds(broadcastPlayTextEvent.getDevices());
            devicePlayTextParam.setPlayOrder(1000);
            devicePlayTextParam.setPlayTime(broadcastPlayTextEvent.getPlayTime());
            String textTaskId = this.broadcastClient.devicePlayText(devicePlayTextParam);
            broadcastPlayTextResponse.setBroadcastPlayStatus("SUCCESS");
            broadcastPlayTextResponse.setPlayTextTaskId(textTaskId);
        } catch (Exception e) {
            log.error("广播播放失败", e);
            broadcastPlayTextResponse.setBroadcastPlayStatus("FAIL");
            broadcastPlayTextResponse.setBroadcastPlayErrorDesc("播放失败");
            broadcastPlayTextResponse.setPlayTextTaskId(null);
        }
        broadcastPlayTextEvent.accept(broadcastPlayTextResponse);
    }
}
