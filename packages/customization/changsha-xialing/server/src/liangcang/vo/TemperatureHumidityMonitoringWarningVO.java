package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 温湿度监测看板最近温度趋势预警。 */
@Data
public class TemperatureHumidityMonitoringWarningVO {

    /** 温度趋势预警记录 ID。 */
    private Long id;
    /** 智慧仓 ID。 */
    private Long warehouseId;
    /** 仓房名称。 */
    private String warehouseName;
    /** 预警触发时间。 */
    private LocalDateTime triggeredAt;
    /** 当前日平均最高温。 */
    private BigDecimal currentAvgTemperature;
    /** 预警阈值。 */
    private BigDecimal warningThreshold;
    /** 距阈值差值。 */
    private BigDecimal distanceToThreshold;
    /** 日均增幅。 */
    private BigDecimal dailyIncrease;
    /** 连续上升天数。 */
    private Integer consecutiveDays;
    /** 预警状态。 */
    private String status;
}
