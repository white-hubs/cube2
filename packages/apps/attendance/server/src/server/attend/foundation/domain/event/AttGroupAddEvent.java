package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.event;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 考勤组新增事件
 */
@Getter
public class AttGroupAddEvent extends ApplicationEvent {
    private final AttGroupPO groupPO;

    public AttGroupAddEvent(Object source, AttGroupPO groupPO) {
        super(source);
        this.groupPO = groupPO;
    }
}
