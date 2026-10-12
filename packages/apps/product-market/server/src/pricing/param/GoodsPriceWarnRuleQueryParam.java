package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 采价预警规则
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnRuleQueryParam extends Paging {

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

}