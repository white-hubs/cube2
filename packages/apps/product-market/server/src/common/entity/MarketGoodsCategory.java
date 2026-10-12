package com.guhecloud.cube.srv.gen.product.market.server.common.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 商品品类
 * @date 2023-05-10 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MarketGoodsCategory extends BasicEnhancedTenantTreeEntity<Long> {

    //主键
    private Long id;
    //商品分类编号
    private String code;
    //商品分类名称
    private String name;
    //是否启用
    private Boolean enabled;
}