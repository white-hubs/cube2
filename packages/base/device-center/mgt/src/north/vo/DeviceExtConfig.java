package com.guhecloud.cube.iot.mgt.north.vo;

import lombok.Data;

@Data
public class DeviceExtConfig {
    /**
     * 扩展配置
     */
    private String config;
    /**
     * 原始设备ID
     */
    private String rawDeviceId;
}
