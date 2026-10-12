package com.guhecloud.cube.base.alarm.server.analysis.vo;

import lombok.Data;

@Data
public class DeviceStatsNumVO {
    /**
     * 设备总数
     */
    private Integer deviceNum;

    /**
     * 在线数
     */
    private Integer normalNumber;

    /**
     * 离线数
     */
    private Integer offlineNumber;

    /**
     * 建档数
     */
    private Long putOnRecordNumber;

    /**
     * 到期数
     */
    private Long expireNumber;
}
