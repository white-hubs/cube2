package com.guhecloud.cube.iot.mgt.core.data.msg.device;

import lombok.Data;

@Data
public class PreDevicePropSetMsg {
    /**
     * 消息ID,通常是commandSeq
     */
    private String commandSeq;
    /**
     * 命令实例ID
     */
    private String instanceId;

    /**
     * 可为空
     */
    private String parentDeviceId;
    private String parentDeviceNumber;

    private long tenant;//租户
    private String deviceId;
    private String deviceName;
    //deviceId or deviceNumber any one
    private String deviceNumber;
    private String propCode;
    private Object propValue;
    private long ts;
}