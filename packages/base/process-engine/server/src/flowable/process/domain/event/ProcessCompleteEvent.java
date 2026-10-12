package com.guhecloud.cube.base.flow.flowable.process.domain.event;

import com.guhecloud.cube.base.flow.flowable.process.domain.ProcessInstanceContext;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author pengjian
 * @date 2022/10/19 18:33
 * @description
 */

@Getter
public class ProcessCompleteEvent extends ApplicationEvent {

    private final ProcessInstanceContext processInstanceContext;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ProcessCompleteEvent(Object source, ProcessInstanceContext processInstanceContext) {
        super(source);
        this.processInstanceContext = processInstanceContext;
    }
}
