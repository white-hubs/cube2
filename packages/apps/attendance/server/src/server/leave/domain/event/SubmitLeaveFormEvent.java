package com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author pengjian
 * @date 2022/11/21 9:38
 * @description 提交请假单事件
 */
@Getter
public class SubmitLeaveFormEvent extends ApplicationEvent {

    private final Long id;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public SubmitLeaveFormEvent(Object source, Long leaveFormId) {
        super(source);
        this.id = leaveFormId;
    }
}
