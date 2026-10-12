package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import java.util.Set;

/**
 * @author wql
 * @describe 设备自定义标签批量绑定
 * {@code @date} 2024/5/17 15:01
 */
@Data
public class TagBatchBindDeviceCmd {
    /**
     * 自定义标签
     */
    private Set<Long> tagIds;
    /**
     * 设备id
     */
    private Set<Long> deviceIds;
}
