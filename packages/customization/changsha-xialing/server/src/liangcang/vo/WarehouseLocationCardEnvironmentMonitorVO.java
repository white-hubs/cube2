package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 货位卡环境与气体监测当前值。 */
@Data
public class WarehouseLocationCardEnvironmentMonitorVO {
    /** 最高温度，单位 ℃。 */
    private BigDecimal maxTemperature;
    /** 最低温度，单位 ℃。 */
    private BigDecimal minTemperature;
    /** 平均温度，单位 ℃。 */
    private BigDecimal avgTemperature;
    /** 二氧化碳 CO2，单位 %vol。 */
    private BigDecimal co2Ppm;
    /** 氧气 O2，单位 %。 */
    private BigDecimal o2Percent;
    /** 磷化氢 PH3，单位 %vol。 */
    private BigDecimal ph3Ppm;
    /** 湿度 RH，单位 %。 */
    private BigDecimal humidity;
}
