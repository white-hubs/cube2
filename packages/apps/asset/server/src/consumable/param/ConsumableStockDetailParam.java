package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 出入库记录明细
 * @date 2023-03-16 10:08:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableStockDetailParam {

    //出入库记录ID
    private Long stockId;
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