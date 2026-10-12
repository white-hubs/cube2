package com.guhecloud.cube.property.management.client.property.param.consumable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/2/5 09:40
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableStockDetailParam {

    /**
     * 仓库id
     */
    private Long storeId;
    //出入库标记(ADD/REDUCE)
    private String stockFlag;
    //耗材ID
    private Long consumableId;
    //数量
    private Integer num;
    //单价
    private BigDecimal price;
    //总金额
    private BigDecimal amount;
    //初始数量
    private Integer initNum;
    //初始金额
    private BigDecimal initAmount;
    private String status;

}
