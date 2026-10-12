package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Appet
 * @Description 采价预警规则
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnRuleVO {

    //
    private Long id;
    //规则名称
    private String name;
    //价格类型(maxmidmin)
    private String priceType;
    //价格条件(>=<=)
    private String compareCondition;
    //比较值
    private BigDecimal compareValue;
    //比较值类型(digitpercentage)
    private String compareValueType;

    private List<Long> goodsCategoryIds;

    private List<String> goodsCategoryNames;

}