package com.guhecloud.cube.biz.mdata.server.goods.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsCategory extends BasicEnhancedTenantTreeEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品分类编号
     */
    private String code;

    /**
     * 商品分类名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
