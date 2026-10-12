package com.guhecloud.cube.base.device.center.metadata.std.dto;

import lombok.Data;

/**
 * 区域内设备数量统计
 */
@Data
public class AreaDeviceNumberDTO {
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 区域id
     */
    private Long areaId;
    /**
     * 区域设备数量
     */
    private Integer deviceNumber;
}
