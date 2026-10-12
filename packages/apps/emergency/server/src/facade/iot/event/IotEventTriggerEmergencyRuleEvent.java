package com.guhecloud.cube.biz.emergency.server.facade.iot.event;

import com.guhecloud.cube.biz.emergency.server.facade.device.response.DeviceInfoResponse;
import com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe.dto.EventTriggerDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class IotEventTriggerEmergencyRuleEvent extends ApplicationEvent {

    private final EventTriggerDTO eventTriggerLog;

    private final List<DeviceInfoResponse> eventDeviceDetailList;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public IotEventTriggerEmergencyRuleEvent(Object source, EventTriggerDTO eventTriggerLog,
                                             List<DeviceInfoResponse> eventDeviceDetailList
                                            ) {
        super(source);
        this.eventTriggerLog = eventTriggerLog;
        this.eventDeviceDetailList = eventDeviceDetailList;
    }
}
