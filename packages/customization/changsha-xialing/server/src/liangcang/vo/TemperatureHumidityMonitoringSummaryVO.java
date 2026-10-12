package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/** 温湿度监测看板顶部统计。 */
@Data
public class TemperatureHumidityMonitoringSummaryVO {

    /** 应用关联设备总数。 */
    private Integer totalDeviceCount;
    /** 应用关联在线设备数。 */
    private Integer onlineDeviceCount;
    /** 应用关联离线设备数。 */
    private Integer offlineDeviceCount;
    /** 存在 ACTIVE 温度预警的仓房数。 */
    private Integer warningWarehouseCount;
    /** 存在设备告警的展示仓房数。 */
    private Integer alarmWarehouseCount;
    /** 应用告警总数。 */
    private Long alarmCount;
    /** 展示仓房温度预警总数。 */
    private Long warningCount;
}
