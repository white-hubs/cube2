package com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust;

import lombok.Data;

import java.util.List;

@Data
public class SimpleRuleRequest extends RuleRequest {

    /**
     * 操作符
     */
    private String propCode;

    /**
     *
     */
    private List<String> oprand;

}
