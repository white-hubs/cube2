package com.guhecloud.cube.biz.metric.server.rule.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RuleDetailVO extends Tree.Simple<Long, RuleDetailVO> {

    private Long id;

    /**
     * 规则项编码
     */
    private String itemCode;

    /**
     * 项名称
     */
    private String itemName;

    /**
     * 项类型
     */
    private String itemType;

    /**
     * 权重
     */
    private Double weightAllot;

    /**
     * 分项code编号
     */
    private String itemCodeConcat;

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
    private List<RuleCoefficientVO> coefficients;
}
