package com.guhecloud.cube.srv.gen.official.management.server.message.event;

import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkPO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 督办事项超期提醒
 *
 * @author appet
 * @date 2023/4/19 20:05
 */
@Getter
public class FocusWorkDelayEvent extends ApplicationEvent {

    private FocusWorkPO focusWork;
    private Integer overTime;//超时

    public FocusWorkDelayEvent(Object source, FocusWorkPO focusWork, Integer overTime) {
        super(source);
        this.focusWork = focusWork;
        this.overTime = overTime;
    }

}
