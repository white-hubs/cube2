package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 环比返回对象
 */
@Data
public class EnergyDailyUsageRingRatioVO {

    /**
     * 当前用量
     */
    private BigDecimal currentDdValue;
    /**
     * 环比用量
     */
    private BigDecimal ringRatioDdValue;
    /**
     * 当前碳排
     */
    private BigDecimal currentDcValue;
    /**
     * 环比碳排
     */
    private BigDecimal ringRatioDcValue;
    /**
     * 当前综合能耗
     */
    private BigDecimal currentZhValue;
    /**
     * 环比综合能耗
     */
    private BigDecimal ringRatioZhValue;

    // 这里可以扩展峰平谷尖的环比
}
