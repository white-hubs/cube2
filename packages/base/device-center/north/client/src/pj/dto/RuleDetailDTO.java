package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class RuleDetailDTO {

    private String ruleName;

    private String ruleNum;

    private JSONObject ruleConfig;

    private boolean enabled;
}
