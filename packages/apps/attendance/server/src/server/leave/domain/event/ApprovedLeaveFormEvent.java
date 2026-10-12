package com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ApprovedLeaveFormEvent extends ApplicationEvent {
    private final Long leaveFormId;

    public ApprovedLeaveFormEvent(Object source, Long leaveFormId) {
        super(source);
        this.leaveFormId = leaveFormId;
    }
}
