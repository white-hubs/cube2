package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 采价计划详情
 * @date 2023-05-16 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsPricingPlanDetail extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //采价计划ID
    private Long planId;
    //商品品类IDS
    private String goodsCategoryIds;
    //商品品类名称
    private String goodsCategoryNames;
    //采价人员IDS
    private String userIds;
    //采价人员姓名
    private String userNames;

}