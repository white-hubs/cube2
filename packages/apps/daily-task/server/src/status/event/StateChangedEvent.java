package com.guhecloud.cube.biz.daily.task.status.event;

import com.guhecloud.cube.biz.daily.task.status.base.TransitionContext;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/10 17:34
 * @Description:
 */
@Getter
public class StateChangedEvent extends ApplicationEvent {
    private final TransitionContext context;

    public StateChangedEvent(TransitionContext context) {
        super(context);
        this.context = context;
    }

}
