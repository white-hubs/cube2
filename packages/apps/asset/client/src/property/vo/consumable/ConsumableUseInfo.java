package com.guhecloud.cube.property.management.client.property.vo.consumable;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/2/5 16:57
 * @Description:
 */
@Data
public class ConsumableUseInfo {

    /**
     * 仓库id
     */
    private Long storeId;
    /**
     * 耗材ID
     */
    private Long id;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 均价
     */
    private BigDecimal avgPrice;
}
