package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDetectionAreaListStatVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 区域模型
     */
    private String modelArea;

    /**
     * 人流量总数
     */
    private Integer totalPersonNumber;
}
