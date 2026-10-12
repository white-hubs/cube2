package com.guhecloud.cube.gm.meattrade.plugin.common.domain.entity;

import lombok.Data;

@Data
public class ThirdPartyGoodsMapping {

    private Long id;

    /**
     * 商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 冷热鲜标识
     */
    private String freshLabel;

    /**
     * 第三方商户标识
     */
    private String thirdPartyGoodsCategory;

    /**
     * 第三方冷热鲜标识
     */
    private String thirdPartyFreshLabel;
}
