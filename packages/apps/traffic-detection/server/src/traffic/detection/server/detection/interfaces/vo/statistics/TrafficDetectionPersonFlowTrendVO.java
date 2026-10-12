package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 累计人次趋势
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionPersonFlowTrendVO {

    /**
     * 时间点
     */
    private String period;

    /**
     * 累计人次
     */
    private Integer totalPersonNumber;
}
