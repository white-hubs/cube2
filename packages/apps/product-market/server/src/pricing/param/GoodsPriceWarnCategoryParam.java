package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 采价预警规则-适用类型
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnCategoryParam {

    //规则ID
    private Long ruleId;
    //品类ID
    private Long goodsCategoryId;
    //父级品类IDS
    private String goodsCategoryIds;

}