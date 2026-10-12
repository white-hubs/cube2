package com.guhecloud.cube.srv.gen.disinfection.server.member.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 定时任务设备取消关联事件（给被取消关联的设备下发“每天 + [0,0]”的空计划）
 */
@Getter
public class DisinfectionScheduledTaskDeviceUnboundEvent extends ApplicationEvent {
    private final Long scheduledTaskId;
    private final List<Long> deviceIds;

    public DisinfectionScheduledTaskDeviceUnboundEvent(Object source, Long scheduledTaskId, List<Long> deviceIds) {
        super(source);
        this.scheduledTaskId = scheduledTaskId;
        this.deviceIds = deviceIds;
    }
}

