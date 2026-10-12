package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.rocketmq.message;

import lombok.Data;

import java.util.Set;

@Data
public class BroadcastPlayRuleTriggerMessage {

    public final static String TOPIC = "BROADCAST_RULE_TRIGGER";

    /**
     * 规则主键
     */
    private Set<Long> ruleIds;
}
