package com.guhecloud.cube.property.management.client.property.vo.property;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyStatisticsVO {
    /**
     * 总金额
     */
    private BigDecimal moneyTotal;
    /**
     * 总数量
     */
    private Integer numberTotal;

}
