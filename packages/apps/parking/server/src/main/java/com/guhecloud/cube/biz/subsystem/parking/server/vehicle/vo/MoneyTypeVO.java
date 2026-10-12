package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyTypeVO {
    /**
     * 支付类型
     */
    private String name;
    private String code;
    /**
     * 实收金额
     */
    private BigDecimal freeAmount;
}
