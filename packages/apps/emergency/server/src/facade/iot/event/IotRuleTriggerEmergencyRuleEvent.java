package com.guhecloud.cube.biz.emergency.server.facade.iot.event;

import com.guhecloud.cube.biz.emergency.server.facade.device.response.DeviceInfoResponse;
import com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe.dto.RuleTriggerDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class IotRuleTriggerEmergencyRuleEvent extends ApplicationEvent {

    private final Long ruleId;

    private final RuleTriggerDTO ruleTriggerLog;

    private final List<DeviceInfoResponse> eventDeviceDetailList;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public IotRuleTriggerEmergencyRuleEvent(Object source, Long ruleId, RuleTriggerDTO ruleTriggerLog,
                                            List<DeviceInfoResponse> eventDeviceDetailList
                                           ) {
        super(source);
        this.ruleId = ruleId;
        this.ruleTriggerLog = ruleTriggerLog;
        this.eventDeviceDetailList = eventDeviceDetailList;
    }
}
