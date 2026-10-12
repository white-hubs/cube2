package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 出入库记录明细
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableStockDetailVO {

    //
    private Long id;
    //出入库记录ID
    private Long stockId;
    //出入库标记(ADDREDUCE)
    private String stockFlag;
    //耗材ID
    private Long consumableId;
    //数量
    //单价
    //总金额

}