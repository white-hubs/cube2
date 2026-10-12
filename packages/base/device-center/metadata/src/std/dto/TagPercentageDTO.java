package com.guhecloud.cube.base.device.center.metadata.std.dto;

import lombok.Data;

@Data
public class TagPercentageDTO {
    /**
     * 标签code
     */
    private String tagCode;
    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 标签下设备数量
     */
    private Integer tagDeviceNumber;
    /**
     * 标签占比
     */
    private Double percentage;
}