package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/** 雷达监测看板顶部统计。 */
@Data
public class RadarMonitoringSummaryVO {

    /** 绑定的雷达设备总数。 */
    private Integer totalDeviceCount;

    /** 在线雷达设备数。 */
    private Integer onlineDeviceCount;

    /** 离线雷达设备数。 */
    private Integer offlineDeviceCount;

    /** 最近告警记录涉及的仓房数。 */
    private Integer alarmWarehouseCount;

    /** 告警中心返回的告警记录总数。 */
    private Long alarmCount;
}
