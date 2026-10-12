package com.guhecloud.cube.iot.mgt.core.data.north.condition;

import lombok.Data;

@Data
public class RuleTriggerEventCondition implements SubscribeCondition {
    private String ruleId;
}
