package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 仓库耗材库存
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableStoreStockQueryParam extends Paging {

    //仓库ID
    private Long storeId;
    //耗材ID
    private Long consumableId;
    //耗材名称
    private String consumableName;
    //库存量
    //存放位置（扩展字段）
    private String position;

}