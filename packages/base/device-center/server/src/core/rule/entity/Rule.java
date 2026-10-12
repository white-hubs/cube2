package com.guhecloud.cube.base.device.center.core.rule.entity;

import lombok.Data;

import java.util.List;

@Data
public class Rule {

    /**
     * 条件字段
     */
    private List<RuleField> ruleFields;
}
