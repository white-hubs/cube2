package com.guhecloud.cube.biz.emergency.server.time.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class DelIotRuleGroupEvent extends ApplicationEvent {

    private final List<Long> ruleIds;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public DelIotRuleGroupEvent(Object source, List<Long> ruleIds) {
        super(source);
        this.ruleIds = ruleIds;
    }
}
