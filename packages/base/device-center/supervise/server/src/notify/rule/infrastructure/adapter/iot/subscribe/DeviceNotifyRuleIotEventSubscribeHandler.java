package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.subscribe;

import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.DeviceRuleConditionTypeEnum;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.event.DeviceNotifyRuleTriggerEvent;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.subscribe.parser.EventTriggerParser;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.DeviceNotifyRuleRepository;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceNotifyRuleIotEventSubscribeHandler {

    private final SubscribeContainer subscribeContainer;

    private final DeviceNotifyRuleRepository deviceNotifyRuleRepository;

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final EventTriggerParser eventTriggerParser;

    @EventListener
    public void subscribeDeviceEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.subscribeContainer.registerDeviceEventMsgHandler((message) -> {
            SessionContextHolder.clearSessionContext();
            //获取报警设备信息
            var deviceDetailDTO = deviceCenterStdClient.getDeviceById(Long.valueOf(message.getDeviceId()));
            if (Objects.isNull(deviceDetailDTO)) {
                log.info("IoT device event triggered, but device not found,deviceId:{}", message.getDeviceId());
                return;
            }
            //根据事件code+设备id获取配置信息
            var deviceNotifyRule = deviceNotifyRuleRepository.findByDeviceId(Long.valueOf(message.getDeviceId()));
            if (Objects.isNull(deviceNotifyRule)) {
                log.info("IoT device event triggered, but device notify rule not found,deviceId:{}", message.getDeviceId());
                return;
            }
            var isMatch = deviceNotifyRule.getConditions().stream()
                    .noneMatch(condition -> condition.getConditionType() == DeviceRuleConditionTypeEnum.DEVICE_EVENT_CONDITION
                                            && condition.getDeviceEventCode().equals(message.getEventCode()));
            if (isMatch) {
                log.info("IoT device event triggered, but notify rule not match,deviceId:{}", message.getDeviceId());
                return;
            }
            //发送规则触发事件
            var deviceNotifyRuleTriggerEvent = new DeviceNotifyRuleTriggerEvent(this, deviceDetailDTO, eventTriggerParser.parseSnapshot(message), deviceNotifyRule);
            this.applicationEventPublisher.publishEvent(deviceNotifyRuleTriggerEvent);
        });
    }
}
