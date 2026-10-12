package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人流量数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDetectionPersonTotalStatVO {

    /**
     * 人流量总数
     */
    private Integer totalNumber;
}
