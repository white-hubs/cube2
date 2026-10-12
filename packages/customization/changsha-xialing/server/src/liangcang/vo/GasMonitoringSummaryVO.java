package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/** 气体监测看板顶部统计。 */
@Data
public class GasMonitoringSummaryVO {

    private Integer totalDeviceCount;
    private Integer onlineDeviceCount;
    private Integer offlineDeviceCount;
    private Integer alarmWarehouseCount;
    private Long alarmCount;
}
