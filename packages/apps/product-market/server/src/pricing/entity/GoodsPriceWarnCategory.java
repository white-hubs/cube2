package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PriceTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Appet
 * @Description 采价预警规则-适用类型
 * @date 2023-05-16 15:08:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GoodsPriceWarnCategory extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;

    private Long groupId;
    //规则ID
    private Long ruleId;
    //价格预警类型
    private PriceTypeEnum priceType;
    //品类ID
    private Long goodsCategoryId;
    //品类名称
    private String goodsCategoryName;
    //父级品类IDS
    private String goodsCategoryIds;

    public GoodsPriceWarnCategory(Long groupId, Long ruleId, PriceTypeEnum priceType, Long goodsCategoryId, String goodsCategoryName,
                                  String goodsCategoryIds
                                 ) {
        this.ruleId = ruleId;
        this.groupId = groupId;
        this.priceType = priceType;
        this.goodsCategoryId = goodsCategoryId;
        this.goodsCategoryName = goodsCategoryName;
        this.goodsCategoryIds = goodsCategoryIds;
    }
}