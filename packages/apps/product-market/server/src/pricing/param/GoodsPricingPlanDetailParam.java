package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Appet
 * @Description 采价计划详情
 * @date 2023-05-16 15:08:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPricingPlanDetailParam {

    //采价计划ID
    private Long planId;
    //商品品类IDS
    private String goodsCategoryIds;
    //采价人员IDS
    private String userIds;

}