package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import lombok.Data;

/**
 * 人流量趋势
 */
@Data
public class TrafficDetectionTrendStatVO {

    /**
     * 时间点
     */
    private String period;

    /**
     * 人流量数量
     */
    private Integer totalPersonNumber;
}
