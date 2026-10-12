package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 采价计划详情
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingPlanDetailVO {

    //
    private Long id;
    //采价计划ID
    private Long planId;
    //商品品类ID
    private Long goodsCategoryId;
    //品类父级IDS
    private String goodsCategoryIds;
    //采价人员IDS
    private String userIds;

}