package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import java.util.Set;

@Data
public class DeviceFilterableByGenreQuery {
    /**
     * 设备名称 模糊查询
     */
    private String name;

    /**
     * 设备id集合 过滤
     */
    private Set<Long> filteredDeviceIds;
    //类型
    private Long deviceType;
}