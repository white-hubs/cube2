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
public class EnergyUsageStatWithQoqVO {

    private String instrumentType;
    private String instrumentTypeName;
    private BigDecimal value;
    private BigDecimal qoqValue;
    private BigDecimal qoqValueRate;

    public BigDecimal getQoqValueRate() {
        if (value != null && qoqValue != null && qoqValue.intValue() != 0) {
            qoqValueRate = value.subtract(qoqValue)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(qoqValue, 2, BigDecimal.ROUND_HALF_UP);
        }
        return qoqValueRate;
    }

}