package com.guhecloud.cube.onecard.server.consumption.vo.statistics;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmountTrendStatVO {

    /**
     * 金额
     */
    @Decimal
    private BigDecimal amount;

    /**
     * 时间
     */
    private String period;
}
