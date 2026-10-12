package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流量检测设备统计
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDetectionDeviceStatVO {

    /**
     * 设备id
     */
    private Long id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备配置
     */
    private String deviceConfig;

    /**
     * 总人流量数量
     */
    private Integer totalPersonNumber;
}
