package com.guhecloud.cube.biz.linkage.server.facade.iot.event;

import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.dto.LinkageIotDeviceProDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class ChangeRuleEvent extends ApplicationEvent {

    private final String oldCode;

    private final String code;

    private final String name;

    private final String op;

    private final Boolean enabled;

    private final List<DeviceProEvent> deviceProEvents;

    private final List<LinkageIotDeviceProDTO> iotDeviceProDTOS;

    public ChangeRuleEvent(Object source, String oldCode, String code, String name, String op, Boolean enabled,
                           List<DeviceProEvent> deviceProEvents,
                           List<LinkageIotDeviceProDTO> iotDeviceProDTOS
                          ) {
        super(source);
        this.oldCode = oldCode;
        this.code = code;
        this.name = name;
        this.op = op;
        this.enabled = enabled;
        this.deviceProEvents = deviceProEvents;
        this.iotDeviceProDTOS = iotDeviceProDTOS;
    }
}
