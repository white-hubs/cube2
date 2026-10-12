package com.guhecloud.cube.srv.gen.property.management.server.consumable.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 耗材分类
 * @date 2023-03-16 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumableCategory extends BasicEnhancedTenantTreeEntity<Long> {

    //
    private Long id;
    //分类名称
    @Unique(composites = "deleted", message = "分类名称不能重复")
    private String name;
}