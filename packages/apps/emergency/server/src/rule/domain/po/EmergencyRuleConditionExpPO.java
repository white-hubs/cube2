package com.guhecloud.cube.biz.emergency.server.rule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.CompareOpEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 预案规则条件表达式实体模型
 *
 * @author lanchun
 * @date 2022/9/3 10:55
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("emergency_rule_condition_exp")
public class EmergencyRuleConditionExpPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /*
    预案规则主键
     */
    private Long emergencyRuleId;

    /**
     * 预案条件主键
     */
    private Long emergencyRuleConditionId;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 属性单位
     */
    private String propUnit;

    /**
     * 比较符（= >=）
     */
    private CompareOpEnum op;

    /**
     * 属性值
     */
    private String oprand;

    /**
     * 延时时间
     */
    private Integer delayTime;

    /**
     * 延迟开始时间
     */
    private Integer startDelayTime;

    /**
     * 延迟结束时间
     */
    private Integer endDelayTime;

    /**
     * 条件组主键
     */
    private Long groupId;
}
