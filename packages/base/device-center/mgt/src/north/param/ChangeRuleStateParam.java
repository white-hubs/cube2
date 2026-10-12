package com.guhecloud.cube.iot.mgt.north.param;

import lombok.Data;

import java.util.List;

@Data
public class ChangeRuleStateParam {

    private List<String> ruleNums;
    private boolean enabled;
}
