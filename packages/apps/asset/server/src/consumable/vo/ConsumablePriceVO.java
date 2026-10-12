package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 耗材入库
 * @date 2023-03-16 10:08:36
 */
@Data
@Builder
public class ConsumablePriceVO {
    //耗材ID
    private Long id;
    //仓库ID
    private Long storeId;
    //单价
    private BigDecimal price;
}