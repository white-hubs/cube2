package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PieChartValueVO {
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private BigDecimal value;
    /**
     * 环比
     */
    private BigDecimal ringRatio;

    public PieChartValueVO() {
    }

    public PieChartValueVO(String name, BigDecimal value, BigDecimal ringRatio) {
        this.name = name;
        this.value = value;
        this.ringRatio = ringRatio;
    }
}
