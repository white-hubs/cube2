package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.Data;

@Data
public class AlarmDeviceStatusStatisticsPageVO {
    private String alarmTargetTypeId;
    private String alarmTargetTypeName;
    /**
     * 报警设备code
     */
    private String deviceCode;
    /**
     * 报警设备Id
     */
    private Long deviceId;

    /**
     * 报警设备名称
     */
    private String deviceName;
    /**
     * 报警设备区域
     */
    private String areaName;

    /**
     * 报警总数
     */
    private Long totalNum;

    /**
     * 待接收报警数
     */
    private Long unConfirmedNum;

    /**
     * 处理中报警数
     */
    private Long progressConfirmedNum;

    /**
     * 已挂起报警数
     */
    private Long suspendNum;

    /**
     * 已处理报警数
     */
    private Long handlerNum;

    /**
     * 超时报警数
     */
    private Long timeoutNum;
}
