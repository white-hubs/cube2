package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 耗材分类
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableCategoryQueryParam extends Paging {

    //分类名称
    private String name;
    //父级ID
    private Long parentId;

}