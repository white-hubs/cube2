package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author appet
 * @Description 每日用量
 * @date 2021-12-30 15:12:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EnergyDailySummary extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    private String dateStr;
    private LocalDateTime statDate;
    private BigDecimal ddValue;
    //碳排
    private BigDecimal dcValue;
    //综合能耗
    private BigDecimal zhValue;
    private BigDecimal fValue;
    private BigDecimal pValue;
    private BigDecimal gValue;
    private BigDecimal jValue;
}