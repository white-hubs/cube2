package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流量检测设备属性
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDetectionDevicePropDTO {

    /**
     * 设备ID
     */
    private Long id;

    /**
     * 设备名称
     */
    private String name;

    private Integer totalPersonNumber;
}
