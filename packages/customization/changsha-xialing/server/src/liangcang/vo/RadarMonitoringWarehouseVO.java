package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 雷达监测看板仓房卡片。 */
@Data
public class RadarMonitoringWarehouseVO {

    /** 智慧仓仓房 id。 */
    private Long warehouseId;

    /** 仓房名称。 */
    private String warehouseName;

    /** 雷达设备 id。 */
    private Long radarDeviceId;

    /** 当前重量。 */
    private BigDecimal currentWeight;

    /** 当前体积。 */
    private BigDecimal currentVolume;

    /** 仓房总容积。 */
    private Double totalVolume;

    /** 设备是否在线。 */
    private Boolean online;

    /** 最后测量或设备通信时间。 */
    private LocalDateTime latestReportTime;

    /** 是否存在告警记录。 */
    private Boolean alarm;

    /** 当前卡片关联的告警内容。 */
    private String alarmMessage;
}
