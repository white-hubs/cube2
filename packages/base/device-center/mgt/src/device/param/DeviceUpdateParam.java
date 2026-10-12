package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;

@Data
public class DeviceUpdateParam {
    private long deviceId;
    /**
     * 设备编码
     */
    private String deviceNumber;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备物联配置
     */
    private String config;
    /**
     * 三方设备ID
     */
    private String rawDeviceId;

    //关联子设备ID集合
    private String relationDeviceIds;
}
