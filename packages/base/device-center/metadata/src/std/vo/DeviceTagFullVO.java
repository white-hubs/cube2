package com.guhecloud.cube.base.device.center.metadata.std.vo;

import lombok.Data;

/**
 * 标签定义
 */
@Data
public class DeviceTagFullVO {

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 标签code
     */
    private String tagCode;

    /**
     * 标签名称
     */
    private String tagName;
}