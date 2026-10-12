package com.guhecloud.cube.biz.subsystem.parking.client.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 柱状图数据实体
 */
@Data
public class StatisticalChartListVO {
    /**
     * 图形Y轴值
     */
    private List<BigDecimal> value;
    /**
     * 图形Y轴名称
     */
    private String name;
    /**
     * 线总数据
     */
    private BigDecimal total;
}
