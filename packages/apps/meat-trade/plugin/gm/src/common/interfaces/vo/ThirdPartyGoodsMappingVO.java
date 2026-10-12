package com.guhecloud.cube.gm.meattrade.plugin.common.interfaces.vo;

import lombok.Data;

/**
 * 第三方商品映射
 *
 * @author lanchun
 * @date 2023/8/8 17:47
 */
@Data
public class ThirdPartyGoodsMappingVO {

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
