package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** 粮仓温度趋势预警采样日数据。 */
@Data
public class WarehouseTemperatureAlertSampleDayVO {
    /** 日期。 */
    private LocalDate date;
    /** 是否完整。 */
    private Boolean complete;
    /** 采样次数。 */
    private Integer sampleCount;
    /** 日平均最高温。 */
    private BigDecimal avgMaxTemperature;
    /** 30 分钟采样项。 */
    private List<Item> items;

    @Data
    public static class Item {
        /** 仓名称。 */
        private String warehouseName;
        /** 采样时间。 */
        private LocalDateTime sampleTime;
        /** 仓内最高温。 */
        private BigDecimal maxTemperature;
    }
}
