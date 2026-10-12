package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 规则触发指标信息
 *
 * @author lanchun
 * @date 2024/9/19 9:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRuleTriggerNorm extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 触发id
     */
    private Long medicalRuleTriggerId;

    /**
     * 上报时间
     */
    private LocalDateTime uploadTime;

    /**
     * 指标值
     */
    private String normValue;

    /**
     * 指标值描述
     */
    private String normValueDescription;

    /**
     * 上限值
     */
    private BigDecimal upperLimit;

    /**
     * 下限值
     */
    private BigDecimal lowerLimit;
    private String normCode;
    private String normName;
    private String unit;
}
