package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 采价计划详情
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingPlanDetailQueryParam extends Paging {

    //采价计划ID
    private Long planId;
    //商品品类ID
    private Long goodsCategoryId;
    //品类父级IDS
    private String goodsCategoryIds;
    //采价人员IDS
    private String userIds;

}