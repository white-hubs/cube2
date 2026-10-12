package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人流详情列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionPersonFlowDetailVO {

    /**
     * 区域ID
     */
    private Long id;

    /**
     * 区域
     */
    private String name;

    /**
     * 区域模型
     */
    private String modelArea;

    /**
     * 区域描述
     */
    private String remarks;

    /**
     * 区域人流量
     */
    private Integer totalPersonNumber;

    /**
     * 上报时间
     */
    private String reportTime;
}
