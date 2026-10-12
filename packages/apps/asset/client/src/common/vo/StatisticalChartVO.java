package com.guhecloud.cube.property.management.client.common.vo;

import lombok.Data;

import java.util.List;

/**
 * 柱状图实体
 */
@Data
public class StatisticalChartVO {
    /**
     * table
     */
    private List<String> xdata;
    /**
     * value
     */
    private List<StatisticalChartListVO> series;

    public StatisticalChartVO(List<String> xdata, List<StatisticalChartListVO> series) {
        this.series = series;
        this.xdata = xdata;
    }
}
