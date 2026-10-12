package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** 粮仓温度趋势预警走势。 */
@Data
public class WarehouseTemperatureAlertTrendVO {
    /** 智慧仓 id。 */
    private Long warehouseId;
    /** 仓名称。 */
    private String warehouseName;
    /** 品种名。 */
    private String grainType;
    /** 预警阈值。 */
    private BigDecimal warningThreshold;
    /** 逼近余量。 */
    private BigDecimal approachMargin;
    /** 逼近线。 */
    private BigDecimal warningLine;
    /** 日平均最高温曲线，按日期升序返回。 */
    private List<Point> series;
    /** 当前激活预警，用于前端直接提示。 */
    private WarehouseTemperatureAlertNoticeVO alertNotice;

    @Data
    public static class Point {
        /** 日期。 */
        private LocalDate date;
        /** 时间。 */
        private LocalDateTime time;
        /** 日平均最高温。 */
        private BigDecimal temperature;
        /** 该日期是否命中过预警触发点。 */
        private Boolean triggered;
    }
}
