package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/** 粮仓三维统计环境历史。 */
@Data
public class WarehouseThreeDimensionalEnvironmentHistoryVO {
    /** 粮仓 id。 */
    private Long warehouseId;
    /** 粮仓名称。 */
    private String warehouseName;
    /** 历史数据点。 */
    private List<Item> items;

    @Data
    public static class Item {
        /** 设备 id。 */
        private Long deviceId;
        /** 采样时间。 */
        private String time;
        /** 温度。 */
        private BigDecimal temperature;
        /** 湿度。 */
        private BigDecimal humidity;
        /** 一氧化碳浓度。 */
        private BigDecimal coPpm;
        /** 二氧化碳浓度。 */
        private BigDecimal co2Ppm;
        /** 一氧化氮浓度。 */
        private BigDecimal noPpm;
        /** 氧气浓度。 */
        private BigDecimal o2PercentVol;
        /** 磷化氢浓度。 */
        private BigDecimal phosphinePpm;
        /** 硫酰氟浓度。 */
        private BigDecimal sulfurylFluoridePpm;
    }
}
