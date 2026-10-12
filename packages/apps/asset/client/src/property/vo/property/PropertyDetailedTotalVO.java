package com.guhecloud.cube.property.management.client.property.vo.property;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyDetailedTotalVO {
    private Integer totalNumber;
    private BigDecimal totalPrice;
}
