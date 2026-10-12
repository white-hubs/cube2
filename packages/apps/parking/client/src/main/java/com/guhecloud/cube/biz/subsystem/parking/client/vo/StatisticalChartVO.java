package com.guhecloud.cube.biz.subsystem.parking.client.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 柱状图实体
 */
@Data
public class StatisticalChartVO {
    /**
     * 环比
     */
    private BigDecimal ringRatio;
    /**
     * table
     */
    private List<String> xdata;
    /**
     * value
     */
    private List<StatisticalChartListVO> series;

    public StatisticalChartVO(BigDecimal ringRatio, List<String> xdata, List<StatisticalChartListVO> series) {
        this.ringRatio = ringRatio;
        this.series = series;
        this.xdata = xdata;
    }
}
