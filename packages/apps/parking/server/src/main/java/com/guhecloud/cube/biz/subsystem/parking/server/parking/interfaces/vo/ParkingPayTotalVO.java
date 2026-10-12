package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 收费统计返回参数
 *
 * @author lanchun
 * @date 2022/10/13 15:23
 */
@Data
public class ParkingPayTotalVO {

    /**
     * 实收金额
     */
    private BigDecimal totalAmount;

    /**
     * 实收金额环比
     */
    private BigDecimal totalAmountQoq;

    /**
     * 应收金额
     */
    private BigDecimal receivableAmount;

    /**
     * 应收金额环比
     */
    private BigDecimal receivableAmountQoq;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 优惠金额环比
     */
    private BigDecimal discountAmountQoq;
}
