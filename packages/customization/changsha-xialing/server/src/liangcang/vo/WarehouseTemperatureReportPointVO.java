package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 粮仓温度报表点位。 */
@Data
public class WarehouseTemperatureReportPointVO {
    /** 电缆号。 */
    private String cableNo;
    /** 点位号。 */
    private String pointNo;
    /** 设备 id。 */
    private Long deviceId;
    /** 设备名称。 */
    private String deviceName;
    /** 实时温度。 */
    private BigDecimal temperature;
}
