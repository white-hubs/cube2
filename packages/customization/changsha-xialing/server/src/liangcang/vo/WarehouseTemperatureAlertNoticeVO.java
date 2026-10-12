package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 粮仓温度趋势预警提示。 */
@Data
public class WarehouseTemperatureAlertNoticeVO {
    /** 是否正在预警。 */
    private Boolean alerting;
    /** 提示文案。 */
    private String message;
    /** 距离阈值差值。 */
    private BigDecimal distanceToThreshold;
    /** 当前日平均最高温。 */
    private BigDecimal currentAvgTemperature;
    /** 阈值。 */
    private BigDecimal warningThreshold;
    /** 预警记录 id。 */
    private Long alertRecordId;
    /** 触发时间。 */
    private LocalDateTime triggeredAt;
}
