package com.guhecloud.cube.biz.emergency.server.facade.iot.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class IotRuleStateChangeEvent extends ApplicationEvent {

    private final Boolean state;

    private final List<Long> ruleIds;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public IotRuleStateChangeEvent(Object source, Boolean state, List<Long> ruleIds) {
        super(source);
        this.state = state;
        this.ruleIds = ruleIds;
    }
}
