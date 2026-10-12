package com.guhecloud.cube.biz.metric.server.rule.command;

import lombok.Data;

import java.util.List;

@Data
public class ChangeRuleConfigCommand {

    private Long id;

    /**
     * 项名称
     */
    private String itemName;

    /**
     * 权重
     */
    private Double weightAllot;

    /**
     * 计算指标
     */
    private Long computeMetric;

    /**
     * 规则内容模版
     */
    private String ruleContentTemplate;

    /**
     * 模版指标变量keys
     */
    private String templateMetricVariables;

    /**
     * 系数区间配置
     */
    private List<SaveRuleCoefficientsCommand> coefficients;
}
