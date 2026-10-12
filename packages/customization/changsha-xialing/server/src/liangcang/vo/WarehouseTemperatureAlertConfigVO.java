package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 粮仓温度趋势预警单仓配置。 */
@Data
public class WarehouseTemperatureAlertConfigVO {
    /** 配置 id。 */
    private Long id;
    /** 智慧仓 id。 */
    private Long warehouseId;
    /** 是否使用全局默认。 */
    private Boolean useGlobalDefault;
    /** 连续上升天数。 */
    private Integer consecutiveDays;
    /** 日均最小增幅。 */
    private BigDecimal minDailyIncrease;
    /** 预警阈值。 */
    private BigDecimal warningThreshold;
    /** 逼近余量。 */
    private BigDecimal approachMargin;
    /** 弹窗开关。 */
    private Boolean popupEnabled;
}
