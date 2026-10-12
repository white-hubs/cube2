package com.guhecloud.cube.srv.gen.energymanagement.server.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 能耗计费规则的用量阶梯对象
 */
@Data
public class StairsEnergyContentsDTO {
    /**
     * 阶梯
     */
    private Integer stairs;
    /**
     * 起始用量(>)
     */
    private BigDecimal startEnergy;
    /**
     * 截止用量(<=)
     */
    private BigDecimal endEnergy;
    /**
     * 单个价格
     */
    private BigDecimal price;
    /**
     * 峰价
     */
    private BigDecimal fvPrice;
    /**
     * 平价
     */
    private BigDecimal pvPrice;
    /**
     * 谷价
     */
    private BigDecimal gvPrice;
    /**
     * 尖价
     */
    private BigDecimal jvPrice;
}
