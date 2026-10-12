package com.guhecloud.cube.iot.mgt.north.vo;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.iot.mgt.north.param.ExtraConfig;
import lombok.Data;

@Data
public class RuleDetailVo {

    private String ruleName;

    private String ruleNum;

    private JSONObject ruleConfig;

    private ExtraConfig extraConfig;

    private boolean enabled;
}
