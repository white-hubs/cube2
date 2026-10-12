package com.guhecloud.cube.biz.linkage.server.facade.iot.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DelRuleEvent extends ApplicationEvent {

    private String ruleNum;

    public DelRuleEvent(Object source, String ruleNum) {
        super(source);
        this.ruleNum = ruleNum;
    }
}
