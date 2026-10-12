package com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe;

import com.guhecloud.cube.biz.emergency.server.facade.device.request.DeviceInfoQueryRequest;
import com.guhecloud.cube.biz.emergency.server.facade.device.response.DeviceInfoResponse;
import com.guhecloud.cube.biz.emergency.server.facade.device.service.EmergencyDeviceInfoQueryService;
import com.guhecloud.cube.biz.emergency.server.facade.iot.event.IotRuleTriggerEmergencyRuleEvent;
import com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe.dto.RuleTriggerDTO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionGroupPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRulePO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleConditionGroupMapper;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleMapper;
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
public class EmergencyRuleIotTriggerSubscribeHandler {

    private final SubscribeContainer subscribeContainer;

    private final EmergencyDeviceInfoQueryService emergencyDeviceInfoQueryService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final EmergencyRuleMapper emergencyRuleMapper;

    private final EmergencyRuleConditionGroupMapper emergencyRuleConditionGroupMapper;

    @EventListener
    public void subscribeRuleTrigger(ApplicationReadyEvent applicationReadyEvent) {
        this.subscribeContainer.registerRuleTriggerEventMsgHandler((ruleTriggerMsg) -> {
            SessionContextHolder.clearSessionContext();
            EmergencyRuleConditionGroupPO emergencyRuleConditionGroupPO = emergencyRuleConditionGroupMapper.selectOneByIgnoreTenant(Long.valueOf(ruleTriggerMsg.getRuleId()));
            EmergencyRulePO emergencyRulePO = emergencyRuleMapper.selectOneByIgnoreTenant(emergencyRuleConditionGroupPO.getEmergencyRuleId());
            SessionContextHolder.requestTenant(emergencyRulePO.getTenant());
            RuleTriggerDTO ruleTriggerDTO = ObjectUtils.copying(ruleTriggerMsg, RuleTriggerDTO.class);
            Long deviceId = Long.valueOf(ruleTriggerDTO.getMatchSnapshot().get(0).getDeviceId());
            //获取报警设备信息
            DeviceInfoResponse deviceInfoResponse = this.emergencyDeviceInfoQueryService.queryDevices(DeviceInfoQueryRequest.builder()
                                                                                                              .deviceId(deviceId)
                                                                                                              .build());
            if (Objects.isNull(deviceInfoResponse)) {
                log.info("IoT rule triggered, but device not found deviceId:{}", deviceId);
                return;
            }
            IotRuleTriggerEmergencyRuleEvent emergencyRuleEvent = new IotRuleTriggerEmergencyRuleEvent(this,
                                                                                                       emergencyRulePO.getId(),
                                                                                                       ruleTriggerDTO,
                                                                                                       Collections.singletonList(deviceInfoResponse));
            this.applicationEventPublisher.publishEvent(emergencyRuleEvent);
        });
    }
}
