package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

import java.math.BigDecimal;

/** 保存粮仓温度趋势预警全局默认配置。 */
@Data
public class WarehouseTemperatureAlertGlobalConfigSaveParam {
    /** 连续上升天数。 */
    private Integer consecutiveDays;
    /** 日均最小增幅。 */
    private BigDecimal minDailyIncrease;
    /** 预警阈值。 */
    private BigDecimal warningThreshold;
    /** 逼近余量。 */
    private BigDecimal approachMargin;
}
