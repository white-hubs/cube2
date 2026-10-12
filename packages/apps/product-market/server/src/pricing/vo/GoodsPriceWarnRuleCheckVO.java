package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PriceTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @author Appet
 * @Description 采价预警规则
 * @date 2023-05-16 15:08:36
 */
@Data
@Builder
public class GoodsPriceWarnRuleCheckVO {
    //规则组ID
    private Long groupId;
    //规则ID
    private Long ruleId;
    //规则名称
    private String ruleName;
    //价格类型
    private PriceTypeEnum priceType;
    //预警描述
    private String warnDesc;
}