package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChargeStatisticsVo {
    private String timeFlag;
    private BigDecimal amount;
    private String instrumentType;
    private BigDecimal fvAmount;
    private BigDecimal pvAmount;
    private BigDecimal gvAmount;
    private String name;
}
