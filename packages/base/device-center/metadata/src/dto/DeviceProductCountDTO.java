package com.guhecloud.cube.base.device.center.metadata.dto;

import lombok.Data;

/**
 * 产品设备数量统计对象
 */
@Data
public class DeviceProductCountDTO {
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 产品名
     */
    private String productName;
    /**
     * 设备数
     */
    private Long deviceCount;
}
