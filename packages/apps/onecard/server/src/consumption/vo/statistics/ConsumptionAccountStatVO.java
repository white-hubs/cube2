package com.guhecloud.cube.onecard.server.consumption.vo.statistics;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsumptionAccountStatVO {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 金额
     */
    @Decimal
    private BigDecimal amount;
}
