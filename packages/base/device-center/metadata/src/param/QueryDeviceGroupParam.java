package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import java.util.Set;

@Data
public class QueryDeviceGroupParam {

    /**
     * 设备id集合
     */
    private Set<Long> deviceIds;

    /**
     * 设备名称
     */
    private String deviceName;
}
