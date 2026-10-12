package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 租控记录利用率统计返回对象
 */
@Data
public class UtilizationRateStatResult {
    /**
     * 总面积
     */
    private BigDecimal totalCalculate;
    /**
     * 使用面积
     */
    private BigDecimal useCalculate;
}
