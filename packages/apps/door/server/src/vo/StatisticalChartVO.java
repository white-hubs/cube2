package com.guhecloud.cube.biz.door.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StatisticalChartVO {
    private BigDecimal previous = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
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
