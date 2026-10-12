package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PriceTypeEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Appet
 * @Description 采价预警规则
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnRuleParam {

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
    //适用品类
    @NotNull(message = "预警规则中的品类不能为空")
    @Size(min = 1, message = "预警规则中的品类数据不能为空")
    private List<Long> goodsCategoryIds;

}