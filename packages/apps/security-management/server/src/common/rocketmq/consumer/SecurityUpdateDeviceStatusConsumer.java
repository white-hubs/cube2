package com.guhecloud.cube.srv.gen.securitymanagement.server.common.rocketmq.consumer;

import com.guhecloud.cube.base.device.center.metadata.lang.DeviceInstanceStatusEnum;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.util.CallbackUtil;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.KeyValueWithTs;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class SecurityUpdateDeviceStatusConsumer {
    @Resource
    private SubscribeContainer subscribeContainer;

    @Resource
    private SecurityCommonMapper securityCommonMapper;

    @EventListener
    public void onSystemReadyEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("系统准备就绪，订阅准备订阅设备状态");

        //同步设备状态
        subscribeContainer.registerDeviceStatusMsgHandler(msg -> {
            log.debug("设备状态消息:====={}", msg);

            if (Objects.isNull(msg)) {
                return;
            }

            List<KeyValueWithTs> updates = msg.getUpdates();
            if (Objects.isNull(updates) || updates.isEmpty()) {
                return;
            }

            String iotDeviceId = msg.getDeviceId();
            boolean onlineSingle = CallbackUtil.isOnlineSingle(msg);
            Boolean deviceStatus = onlineSingle;
            String communicationStatus =
                    onlineSingle == true ? DeviceInstanceStatusEnum.ONLINE.getName() : DeviceInstanceStatusEnum.OFFLINE.getName();
            securityCommonMapper.updateDeviceStatus(Long.parseLong(iotDeviceId), deviceStatus, communicationStatus);
            log.debug("状态更新结束======原始状态===" + onlineSingle);
        });

    }
}
