package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureTrendScope;
import lombok.Data;

import java.time.LocalDateTime;

/** 仓库粮温趋势统计查询。 */
@Data
public class WarehouseTemperatureTrendStatisticsParam {

    /** 智慧仓库粮仓 ID。 */
    private Long warehouseId;
    /** 曲线统计范围，默认全仓。 */
    private WarehouseTemperatureTrendScope scope;
    /** 预测天数：7 或 15，默认 7。 */
    private Integer forecastDays;
    /** 统计截止时间，默认当前时间。 */
    private LocalDateTime endTime;
}
