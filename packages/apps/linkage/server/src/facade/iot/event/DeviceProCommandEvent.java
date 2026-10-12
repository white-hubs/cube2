package com.guhecloud.cube.biz.linkage.server.facade.iot.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 设备属性写字事件
 *
 * @author lanchun
 * @date 2022/6/25 10:23
 */
@Getter
public class DeviceProCommandEvent extends ApplicationEvent {

    private List<ChangeDeviceProEvent> changeDeviceProEvents;

    public DeviceProCommandEvent(Object source, List<ChangeDeviceProEvent> changeDeviceProEvents) {
        super(source);
        this.changeDeviceProEvents = changeDeviceProEvents;
    }
}
