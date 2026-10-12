package com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust;

import lombok.Data;

import java.util.List;

/**
 * @author lanchun
 * @date 2022/6/22 14:13
 */
@Data
public class ComboRuleRequestRequest extends RuleRequest {

    /**
     * 规则集合
     */
    private List<Object> rules;
}
