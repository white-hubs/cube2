package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 环比对象
 */
@Data
public class EnergyChargeRatioVO {
    private BigDecimal current;
    private BigDecimal before;
    private BigDecimal ringRatio;
}
