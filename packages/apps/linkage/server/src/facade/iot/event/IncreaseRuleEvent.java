package com.guhecloud.cube.biz.linkage.server.facade.iot.event;

import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.dto.LinkageIotDeviceProDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class IncreaseRuleEvent extends ApplicationEvent {

    private String code;

    private String name;

    private String op;

    private Boolean enabled;

    private List<DeviceProEvent> deviceProEvents;

    private List<LinkageIotDeviceProDTO> iotDeviceProDTOS;

    public IncreaseRuleEvent(Object source, String code, String name, String op, Boolean enabled, List<DeviceProEvent> deviceProEvents,
                             List<LinkageIotDeviceProDTO> iotDeviceProDTOS
                            ) {
        super(source);
        this.code = code;
        this.name = name;
        this.op = op;
        this.enabled = enabled;
        this.deviceProEvents = deviceProEvents;
        this.iotDeviceProDTOS = iotDeviceProDTOS;
    }
}