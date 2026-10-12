package com.guhecloud.cube.srv.gen.product.market.server.common.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 商品品类
 * @date 2023-05-10 15:08:36
 */
@Data
public class MarketGoodsCategoryParam {

    //商品分类编号
    private String code;
    //商品分类名称
    private String name;
    //商品分类层级
    //父类主键
    //序列号
    private String sequence;
    //是否启用

}