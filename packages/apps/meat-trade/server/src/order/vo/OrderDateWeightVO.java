package com.guhecloud.cube.biz.meattrade.server.order.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDateWeightVO {

    private String summaryDate;

    /**
     * 交易重量
     */
    @Decimal
    private BigDecimal summaryWeight;
}
