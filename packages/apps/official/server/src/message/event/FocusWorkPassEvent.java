package com.guhecloud.cube.srv.gen.official.management.server.message.event;

import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkPO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 督办事项审批通过事件
 *
 * @author appet
 * @date 2023/4/19 20:05
 */
@Getter
public class FocusWorkPassEvent extends ApplicationEvent {

    private FocusWorkPO focusWork;

    public FocusWorkPassEvent(Object source, FocusWorkPO focusWork) {
        super(source);
        this.focusWork = focusWork;
    }

}
