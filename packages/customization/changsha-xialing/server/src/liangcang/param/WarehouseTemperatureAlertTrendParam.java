package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

import java.time.LocalDate;

/** 粮仓温度趋势预警走势查询。 */
@Data
public class WarehouseTemperatureAlertTrendParam {
    /** 智慧仓 id。 */
    private Long warehouseId;
    /** 开始日期。 */
    private LocalDate startDate;
    /** 结束日期。 */
    private LocalDate endDate;
}
