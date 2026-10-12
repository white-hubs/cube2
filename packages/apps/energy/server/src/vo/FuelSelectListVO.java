package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 燃料类型
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FuelSelectListVO {
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
