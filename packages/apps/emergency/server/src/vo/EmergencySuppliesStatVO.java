package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 物资统计
 * @date 2024-01-26 10:08:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencySuppliesStatVO {
    private Long id;
    private String name;
    private BigDecimal inValue;
    private BigDecimal outValue;
    private BigDecimal stockValue;
    //占比
    private BigDecimal percentage;

    public BigDecimal getStockValue() {
        BigDecimal stockValue = inValue.subtract(outValue);
        return stockValue.compareTo(BigDecimal.ZERO) > 0 ? stockValue : BigDecimal.ZERO;
    }
}