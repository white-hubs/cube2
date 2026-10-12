package com.guhecloud.cube.iot.north.bridge.client.pj.param;

import lombok.Data;

import java.util.List;

@Data
public class ChangeRuleStateParam {

    private List<String> ruleNums;
    private boolean enabled;
}
