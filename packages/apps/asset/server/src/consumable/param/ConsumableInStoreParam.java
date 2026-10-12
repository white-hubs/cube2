package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Appet
 * @Description 耗材管理
 * @date 2023-03-16 10:08:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableInStoreParam extends Paging {
    //关键字
    private String keyword;
    //仓库ID
    private Long storeId;
    //耗材类型
    private Long categoryId;
    //库存类型
    private String type;
}