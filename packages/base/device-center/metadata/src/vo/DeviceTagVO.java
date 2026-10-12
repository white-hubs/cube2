package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.Data;

/**
 * 标签定义
 */
@Data
public class DeviceTagVO {
    private Long id;

    /**
     * 标签类别
     */
    private Long tagTypeId;

    /**
     * 标签code
     */
    private String tagCode;

    /**
     * 标签名称
     */
    private String tagName;
}