package com.guhecloud.cube.iot.north.bridge.client.pj.param;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class RuleCreateParam {
    private String tenantId;

    private String ruleName;

    private String ruleNum;

    private JSONObject ruleConfig;

    private ExtraConfig extraConfig;

    private String operator;

    private String enabled;
}
