package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 耗材分类
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableCategoryVO {

    //
    private Long id;
    //分类名称
    private String name;
    //父级ID
    private Long parentId;
    //父级序列
    private String sequence;

}