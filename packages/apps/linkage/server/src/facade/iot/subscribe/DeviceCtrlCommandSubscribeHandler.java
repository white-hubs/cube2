package com.guhecloud.cube.biz.linkage.server.facade.iot.subscribe;

import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.handler.LinkageIotDeviceHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Slf4j
@Component
public class DeviceCtrlCommandSubscribeHandler {

    @Resource
    private SubscribeContainer subscribeContainer;

    @Autowired
    private LinkageIotDeviceHandler linkageIotDeviceHandler;

    @EventListener
    public void subscribeDeviceCtrlCommandResponse(ApplicationReadyEvent applicationReadyEvent) {
        this.subscribeContainer.registerDeviceCommandMsgHandler((commandResponse) -> {
            try {
                this.linkageIotDeviceHandler.updateCommandStatus(commandResponse.getCommandResponse());
            } catch (Exception e) {
                log.error("回调修改设备属性控制结果失败,命令id:{},失败原因:{}", commandResponse.getInstanceId(), e);
            }
        });
    }
}
