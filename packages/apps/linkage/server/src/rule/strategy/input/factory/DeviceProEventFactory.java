package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.factory;

import com.guhecloud.cube.biz.linkage.server.facade.iot.event.DeviceProEvent;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dto.RuleDeviceAlarmDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dto.RuleDeviceConditionDTO;

public class DeviceProEventFactory {

    public static DeviceProEvent getDeviceProEvent(RuleDeviceAlarmDTO ruleDeviceAlarmDTO, RuleDeviceConditionDTO ruleDeviceConditionDTO) {
        return DeviceProEvent.builder()
                .deviceCode(String.valueOf(ruleDeviceConditionDTO.getId()))
                .delayTime(ruleDeviceAlarmDTO.getDelayTime())
                .op(ruleDeviceAlarmDTO.getOp())
                .oprand(ruleDeviceAlarmDTO.getOprand())
                .propCode(ruleDeviceAlarmDTO.getPropCode())
                .build();
    }

    public static DeviceProEvent getDeviceProEvent(RuleDeviceConditionDTO ruleDeviceConditionDTO) {
        return DeviceProEvent.builder()
                .deviceCode(String.valueOf(ruleDeviceConditionDTO.getId()))
                .delayTime(ruleDeviceConditionDTO.getDelayTime())
                .op(ruleDeviceConditionDTO.getOp())
                .oprand(ruleDeviceConditionDTO.getOprand())
                .propCode(ruleDeviceConditionDTO.getPropCode())
                .build();
    }
}
