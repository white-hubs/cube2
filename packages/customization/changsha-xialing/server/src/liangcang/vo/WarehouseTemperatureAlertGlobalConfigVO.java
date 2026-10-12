package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 粮仓温度趋势预警全局默认配置。 */
@Data
public class WarehouseTemperatureAlertGlobalConfigVO {
    /** 配置 id。 */
    private Long id;
    /** 连续上升天数。 */
    private Integer consecutiveDays;
    /** 日均最小增幅。 */
    private BigDecimal minDailyIncrease;
    /** 预警阈值。 */
    private BigDecimal warningThreshold;
    /** 逼近余量。 */
    private BigDecimal approachMargin;
}
