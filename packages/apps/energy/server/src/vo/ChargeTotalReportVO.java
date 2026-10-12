package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ChargeTotalReportVO {
    private LocalDateTime statDate;
    private String statDateStr;
    /**
     * 总费用
     */
    private BigDecimal totalCost;
    /**
     * 电费
     */
    private BigDecimal electricityCost;
    /**
     * 水费
     */
    private BigDecimal waterCost;
    /**
     * 燃气费
     */
    private BigDecimal gasCost;

    public BigDecimal getTotalCost() {
        BigDecimal ec = electricityCost == null ? BigDecimal.ZERO : electricityCost;
        return ec.add(waterCost == null ? BigDecimal.ZERO : waterCost).add(gasCost == null ? BigDecimal.ZERO : gasCost);
    }
}
