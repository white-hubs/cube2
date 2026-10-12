package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PriceTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 采价预警规则
 * @date 2023-05-16 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsPriceWarnRule extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //告警组ID
    private Long groupId;
    //规则名称
    private String name;
    //价格类型(maxmidmin)
    private PriceTypeEnum priceType;
    //价格条件(>=<=)
    private String compareCondition;
    //比较值
    private BigDecimal compareValue;
    //比较值类型(digitpercentage)
    private String compareValueType;

}