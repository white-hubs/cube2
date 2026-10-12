package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyOverviewVO {
    /**
     * 实收总金额
     */
    private BigDecimal freeAmount;
    /**
     * 应收总金额
     */
    private BigDecimal receivableAmount;
    /**
     * 免费金额
     */
    private BigDecimal discountAmount;
    /**
     * 实收总金额环比
     */
    private BigDecimal freeAmountRingRatio;
    /**
     * 应收总金额比
     */
    private BigDecimal receivableAmountRingRatio;
    /**
     * 免费金额环比
     */
    private BigDecimal discountAmountRingRatio;

    public MoneyOverviewVO() {
    }

    public MoneyOverviewVO(BigDecimal freeAmount, BigDecimal receivableAmount, BigDecimal discountAmount) {
        this.freeAmount = freeAmount;
        this.receivableAmount = receivableAmount;
        this.discountAmount = discountAmount;
        BigDecimal initial = new BigDecimal(0);
        this.discountAmountRingRatio = initial;
        this.freeAmountRingRatio = initial;
        this.receivableAmountRingRatio = initial;
    }

}
