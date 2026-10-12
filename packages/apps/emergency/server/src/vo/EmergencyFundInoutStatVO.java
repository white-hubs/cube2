package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 应急资金收支统计
 * @date 2024-01-22 10:08:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyFundInoutStatVO {
    private String day;
    private BigDecimal inAmount;//每日收入
    private BigDecimal outAmount;//每日支出
    private BigDecimal inAmountTotal;//当前累计收入
    private BigDecimal outAmountTotal;//当前累计支出
    //当日累计结余
    private BigDecimal currentAmountTotal;
    //前一日累计结余
    private BigDecimal preAmountTotal;

    public BigDecimal getCurrentAmountTotal() {
        if (inAmountTotal != null && outAmountTotal != null) {
            currentAmountTotal = inAmountTotal.subtract(outAmountTotal);
        }
        return currentAmountTotal;
    }

    public BigDecimal getPreAmountTotal() {
        if (inAmountTotal != null && outAmountTotal != null) {
            preAmountTotal = inAmountTotal.subtract(inAmount).subtract(outAmountTotal.subtract(outAmount));
        }
        return preAmountTotal;
    }

    public EmergencyFundInoutStatVO(String day, BigDecimal inAmount, BigDecimal outAmount) {
        this.day = day;
        this.inAmount = inAmount;
        this.outAmount = outAmount;
    }
}