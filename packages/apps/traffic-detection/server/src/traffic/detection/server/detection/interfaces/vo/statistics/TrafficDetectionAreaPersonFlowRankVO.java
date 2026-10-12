package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 区域人流排行
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionAreaPersonFlowRankVO {

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
     * 备注
     */
    private String remarks;

    /**
     * 累计人次
     */
    private Integer totalPersonNumber;

    /**
     * 均人次
     */
    private Integer avgPersonNumber;

    /**
     * 峰值人数
     */
    private Integer peakPersonNumber;

    /**
     * 峰值出现时间
     */
    private String peakPeriod;
}
