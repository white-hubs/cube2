package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import java.util.Set;

/**
 * @author wql
 * @describe 设备绑定自定义标签
 * &#064;date  2024/5/17 16:11
 */
@Data
public class DeviceBindTagsCmd {
    /**
     * 自定义标签
     */
    private Set<Long> tagIds;
    /**
     * 设备id
     */
    private Long deviceId;
}
