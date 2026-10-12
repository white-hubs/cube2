package com.guhecloud.cube.biz.metric.server.rule.command;

import com.guhecloud.cube.biz.metric.server.rule.enumeration.RuleItemTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class IncreaseRuleConfigCommand {

    /**
     * 规则项编码
     */
    private String itemCode;

    /**
     * 项名称
     */
    private String itemName;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 项类型
     */
    private RuleItemTypeEnum itemType;

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
