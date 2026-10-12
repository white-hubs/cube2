package com.guhecloud.cube.biz.metric.server.rule.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 规则系数
 *
 * @author lanchun
 * @date 2025/6/3 15:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "rule_coefficient")
public class RuleCoefficientPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则主键
     */
    private Long ruleId;

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
