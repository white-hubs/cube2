package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 气体监测看板仓房卡片。 */
@Data
public class GasMonitoringWarehouseVO {

    private Long warehouseId;
    private String warehouseName;
    private BigDecimal co2Ppm;
    private BigDecimal o2Percent;
    private BigDecimal ph3Ppm;
    private Boolean online;
    private Boolean alarm;
    private String alarmMessage;
}
