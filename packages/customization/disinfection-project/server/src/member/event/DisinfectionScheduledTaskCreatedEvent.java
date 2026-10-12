package com.guhecloud.cube.srv.gen.disinfection.server.member.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 定时任务创建事件（当设备列表不为空时发送，后续可在此事件中实现定时任务调度等逻辑）
 */
@Getter
public class DisinfectionScheduledTaskCreatedEvent extends ApplicationEvent {
    /**
     * 定时任务主键 id
     */
    private final Long scheduledTaskId;

    public DisinfectionScheduledTaskCreatedEvent(Object source, Long scheduledTaskId) {
        super(source);
        this.scheduledTaskId = scheduledTaskId;
    }
}
