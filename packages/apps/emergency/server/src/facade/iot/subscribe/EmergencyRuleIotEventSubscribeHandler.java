package com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe;

import com.guhecloud.cube.biz.emergency.server.facade.device.request.DeviceInfoQueryRequest;
import com.guhecloud.cube.biz.emergency.server.facade.device.response.DeviceInfoResponse;
import com.guhecloud.cube.biz.emergency.server.facade.device.service.EmergencyDeviceInfoQueryService;
import com.guhecloud.cube.biz.emergency.server.facade.iot.event.IotEventTriggerEmergencyRuleEvent;
import com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe.dto.EventTriggerDTO;
import com.guhecloud.cube.biz.emergency.server.rule.application.querygateway.EmergencyRuleQueryService;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleIotEventSubscribeHandler {

    private final SubscribeContainer subscribeContainer;

    private final EmergencyDeviceInfoQueryService emergencyDeviceInfoQueryService;

    private final EmergencyRuleQueryService emergencyRuleQueryService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void subscribeDeviceEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.subscribeContainer.registerDeviceEventMsgHandler((message) -> {
            SessionContextHolder.clearSessionContext();
            EventTriggerDTO eventTriggerDTO = ObjectUtils.copying(message, EventTriggerDTO.class);
            eventTriggerDTO.setRelatedProp(message.getRelatedProp());
            Integer tenant = this.emergencyRuleQueryService.getEventEmergencyTenant(message.getEventCode(), Long.parseLong(message.getDeviceId()));
            if (Objects.isNull(tenant)) {
                log.info("IoT device event triggered, but emergency rule not found,eventCode:{},deviceId:{}", message.getEventCode(), message.getDeviceId());
            }
            SessionContextHolder.requestTenant(tenant);
            //获取报警设备信息
            DeviceInfoResponse deviceInfoResponse = this.emergencyDeviceInfoQueryService.queryDevices(DeviceInfoQueryRequest.builder()
                                                                                                              .deviceId(Long.valueOf(eventTriggerDTO.getDeviceId()))
                                                                                                              .build());
            if (Objects.isNull(deviceInfoResponse)) {
                log.info("IoT device event triggered, but device not found,deviceId:{}", eventTriggerDTO.getDeviceId());
                return;
            }
            IotEventTriggerEmergencyRuleEvent emergencyRuleEvent = new IotEventTriggerEmergencyRuleEvent(this,
                                                                                                         eventTriggerDTO,
                                                                                                         Collections.singletonList(deviceInfoResponse));
            this.applicationEventPublisher.publishEvent(emergencyRuleEvent);
        });
    }
}
