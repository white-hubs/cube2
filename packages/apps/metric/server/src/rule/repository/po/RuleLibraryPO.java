package com.guhecloud.cube.biz.metric.server.rule.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "rule_library")
public class RuleLibraryPO extends BasicEnhancedTenantTreeEntity<Long> {

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
}
