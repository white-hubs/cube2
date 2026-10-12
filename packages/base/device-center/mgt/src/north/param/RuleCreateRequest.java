package com.guhecloud.cube.iot.mgt.north.param;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @author mioopp
 * @create 2021-11-22 16:43
 */
@Data
public class RuleCreateRequest {

    private String tenantId;

    private String ruleName;

    private String ruleNum;

    private JSONObject ruleConfig;

    private ExtraConfig extraConfig;

    private String operator;

    private String enabled;

}