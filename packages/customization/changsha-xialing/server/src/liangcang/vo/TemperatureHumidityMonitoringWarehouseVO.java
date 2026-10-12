package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 温湿度监测看板仓房卡片。 */
@Data
public class TemperatureHumidityMonitoringWarehouseVO {

    /** 智慧仓 ID。 */
    private Long warehouseId;
    /** 仓房名称。 */
    private String name;
    /** 仓房绑定的应用在线设备数。 */
    private Integer onlineDeviceCount;
    /** 仓房绑定的应用离线设备数。 */
    private Integer offlineDeviceCount;
    /** 是否至少有一台绑定应用设备在线。 */
    private Boolean online;
    /** 仓房最高温度。 */
    private BigDecimal maxTemperature;
    /** 仓房最低温度。 */
    private BigDecimal minTemperature;
    /** 仓房平均温度。 */
    private BigDecimal avgTemperature;
    /** 仓房绑定温湿度设备的最新湿度平均值。 */
    private BigDecimal humidity;
    /** 是否存在设备告警。 */
    private Boolean alarm;
    /** 是否存在 ACTIVE 温度预警。 */
    private Boolean warning;
}
