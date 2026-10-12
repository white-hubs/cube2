package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author Appet
 * @Description 耗材分类
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableCategoryParam {

    //分类名称
    @NotEmpty(message = "名称不能为空")
    private String name;
    //父级ID
    private Long parentId;

}