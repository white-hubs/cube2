package com.guhecloud.cube.sn.meattrade.plugin.common.entity;

import lombok.Data;

@Data
public class ThirdPartyGoodsMapping {

    private Long id;

    /**
     * 商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 第三方商户标识
     */
    private String thirdPartyGoodsCategory;
}
