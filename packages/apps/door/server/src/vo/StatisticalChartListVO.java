package com.guhecloud.cube.biz.door.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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
     * 总数
     */
    private BigDecimal total;
}
