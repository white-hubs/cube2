package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 费用统计(每日)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyChargeReport extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private LocalDateTime statDate;
    private String statDateStr;
    /**
     * 用能类型code(InstrumentTypeEnum)
     */
    private String instrumentType;
    /**
     * 用能类型名称
     */
    private String instrumentTypeName;
    private Long areaId;
    private String areaName;
    private String sequence;
    private BigDecimal totalCost;
    private BigDecimal dvCost;
    private BigDecimal fvCost;
    private BigDecimal pvCost;
    private BigDecimal gvCost;
    private BigDecimal jvCost;
}
