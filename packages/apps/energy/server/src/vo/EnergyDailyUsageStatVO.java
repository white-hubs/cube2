package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author appet
 * @Description 每日用量
 * @date 2021-12-30 15:12:36
 */
@Data
@Builder
public class EnergyDailyUsageStatVO {

    private String instrumentType;
    private BigDecimal dayValue;
    private BigDecimal monthValue;
    private BigDecimal yearValue;

    private BigDecimal qoqDayValue;
    private BigDecimal qoqMonthValue;
    private BigDecimal qoqYearValue;

    private BigDecimal qoqDayValueRate;
    private BigDecimal qoqMonthValueRate;
    private BigDecimal qoqYearValueRate;

    public BigDecimal getQoqDayValueRate() {
        if (dayValue != null && qoqDayValue != null && qoqDayValue.compareTo(BigDecimal.ZERO) > 0) {
            qoqDayValueRate = dayValue.subtract(qoqDayValue)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(qoqDayValue, 2, BigDecimal.ROUND_HALF_UP);
        }
        return qoqDayValueRate;
    }

    public BigDecimal getQoqMonthValueRate() {
        if (monthValue != null && qoqMonthValue != null && qoqMonthValue.compareTo(BigDecimal.ZERO) > 0) {
            qoqMonthValueRate = monthValue.subtract(qoqMonthValue)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(qoqMonthValue, 2, BigDecimal.ROUND_HALF_UP);
        }
        return qoqMonthValueRate;
    }

    public BigDecimal getQoqYearValueRate() {
        if (yearValue != null && qoqYearValue != null && qoqYearValue.compareTo(BigDecimal.ZERO) > 0) {
            qoqYearValueRate = yearValue.subtract(qoqYearValue)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(qoqYearValue, 2, BigDecimal.ROUND_HALF_UP);
        }
        return qoqYearValueRate;
    }
}