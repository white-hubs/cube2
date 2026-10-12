package com.guhecloud.cube.property.management.client.property.vo.property;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyNumVO {
    private BigDecimal totalMoney;
    private Integer totalNum;

    public MoneyNumVO(BigDecimal totalMoney, Integer totalNum) {
        this.totalMoney = totalMoney;
        this.totalNum = totalNum;
    }
}
