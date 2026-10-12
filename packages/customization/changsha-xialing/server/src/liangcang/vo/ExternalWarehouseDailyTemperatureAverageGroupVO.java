package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/** 绑定粮仓日温度均值，按仓库分组。 */
@Data
public class ExternalWarehouseDailyTemperatureAverageGroupVO {
    private Long warehouseId;
    private String warehouseName;
    private List<DailyTemperatureAverageItem> items;

    @Data
    public static class DailyTemperatureAverageItem {
        private String date;
        private BigDecimal avgTemperature;
    }
}
