package com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust;

import lombok.Data;

import java.util.List;

/**
 * JSON 编码的规则定义
 *
 * @author lanchun
 * @date 2022/6/22 14:00
 */
@Data
public class RuleConfigRequest {

    /**
     * 动作
     */
    private List<Object> action;

    /**
     * 规则
     */
    private ComboRuleRequestRequest rule;
}
