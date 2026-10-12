package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 商品采价计划
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingPlanQueryParam extends Paging {
    //计划名称
    private String name;
    //紧急程度
    private String urgency;
    //发布类型
    private String publishType;
    //时间段
    private Period period;
    //状态
    private String status;

}