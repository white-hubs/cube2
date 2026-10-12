package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 人流统计汇总
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionPersonFlowSummaryVO {

    /**
     * 累计人次
     */
    private Integer totalPersonNumber;

    /**
     * 累计人次环比百分比
     */
    private BigDecimal totalMomRate;

    /**
     * 累计人次同比百分比
     */
    private BigDecimal totalYoyRate;

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
