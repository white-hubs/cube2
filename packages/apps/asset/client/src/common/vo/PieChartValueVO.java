package com.guhecloud.cube.property.management.client.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 饼状图实体
 */
@Data
public class PieChartValueVO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private BigDecimal value;
    /**
     * 环比
     */
    private BigDecimal ringRatio;
    /**
     * 百分比
     */
    private BigDecimal totalRatio;

    public PieChartValueVO() {
    }

    public PieChartValueVO(String name, BigDecimal value) {
        this.name = name;
        this.value = value;
        this.ringRatio = new BigDecimal(0);
        this.totalRatio = new BigDecimal(0);
    }

    public PieChartValueVO(String name, BigDecimal value, BigDecimal ringRatio, BigDecimal totalRatio) {
        this.name = name;
        this.value = value;
        this.ringRatio = ringRatio;
        this.totalRatio = totalRatio;
    }
}
