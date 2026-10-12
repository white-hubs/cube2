package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 粮仓温度趋势预警弹窗数据。 */
@Data
public class WarehouseTemperatureAlertPopupVO {
    /** 预警记录 id。 */
    private Long alertRecordId;
    /** 智慧仓 id。 */
    private Long warehouseId;
    /** 仓名称。 */
    private String warehouseName;
    /** 品种名。 */
    private String grainType;
    /** 当前日平均最高温。 */
    private BigDecimal currentAvgTemperature;
    /** 阈值。 */
    private BigDecimal warningThreshold;
    /** 逼近余量。 */
    private BigDecimal approachMargin;
    /** 距阈值差值。 */
    private BigDecimal distanceToThreshold;
    /** 触发时间。 */
    private LocalDateTime triggeredAt;
    /** 提示文案。 */
    private String message;
}
