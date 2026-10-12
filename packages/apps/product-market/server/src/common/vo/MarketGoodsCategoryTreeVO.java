package com.guhecloud.cube.srv.gen.product.market.server.common.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketGoodsCategoryTreeVO extends Tree.Simple<Long, MarketGoodsCategoryTreeVO> {

    /**
     * 商品分类编码
     */
    private String code;

    /**
     * 商品分类名称
     */
    private String name;
}
