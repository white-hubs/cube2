package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * @author Appet
 * @Description 耗材分类
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableCategoryTreeVO extends Tree.Simple<Long, ConsumableCategoryTreeVO> {

    //分类名称
    private String name;

}