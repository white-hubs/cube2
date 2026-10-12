package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class FuelDischargeStandard extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 燃料code
     */
    private String code;
    /**
     * 燃料名称
     */
    private String fuelName;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 碳排因子
     */
    private BigDecimal carbonFactor;
    /**
     * 计算系数
     */
    private BigDecimal calculateProportion;
}
