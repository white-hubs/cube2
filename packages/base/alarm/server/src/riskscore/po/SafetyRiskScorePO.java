package com.guhecloud.cube.base.alarm.server.riskscore.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 分值区间
 * @date 2023/10/12 15:29
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "safety_risk_score")
public class SafetyRiskScorePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 指标类型
     */
    private String indexType;
    /**
     * 区间开始值
     */
    private Double sectionStart;
    /**
     * 区间结束值
     */
    private Double sectionEnd;
    /**
     * 斜率
     */
    private Double slope;
    /**
     * 分数开始值
     */
    private Integer scoreStart;
    /**
     * 分数结束值
     */
    private Integer scoreEnd;
}
