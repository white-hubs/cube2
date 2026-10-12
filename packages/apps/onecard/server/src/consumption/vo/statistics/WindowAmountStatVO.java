package com.guhecloud.cube.onecard.server.consumption.vo.statistics;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WindowAmountStatVO {

    /**
     * 窗口名称
     */
    private String windowName;

    /**
     * 窗口金额
     */
    @Decimal
    private BigDecimal amount;
}
