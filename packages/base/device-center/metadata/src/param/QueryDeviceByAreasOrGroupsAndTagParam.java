package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import java.util.Set;

@Data
public class QueryDeviceByAreasOrGroupsAndTagParam {

    /**
     * 标签编号
     */
    private String tagCode;

    /**
     * 区域id集合
     */
    private Set<Long> areaIds;

    /**
     * 分组id集合
     */
    private Set<Long> groupIds;

    /**
     * 设备id集合
     */
    private Set<Long> deviceIds;
}
