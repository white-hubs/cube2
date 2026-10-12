package com.guhecloud.cube.biz.metric.server.rule.command;

import lombok.Data;

@Data
public class SaveRuleCoefficientsCommand {

    /**
     * 大于
     */
    private Double greaterThan;

    /**
     * 小于等于
     */
    private Double lessThanOrEqual;

    /**
     * 得分占比
     */
    private Double scoreRatio;
}
