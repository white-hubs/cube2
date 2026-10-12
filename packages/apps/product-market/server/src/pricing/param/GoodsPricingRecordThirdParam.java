package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsPricingRecordThirdParam extends Paging {
    /**
     * 采价人员名称
     */
    private String name;
    /**
     * 商品品类ID
     */
    private Long goodsCategoryId;
    /**
     * 品类名称
     */
    private String goodsCategoryName;
    /**
     * 采价时间范围
     */
    private Period period;

    /**
     * 时间段不为空
     *
     * @return true表示不为空
     */
    public boolean hasPeriod() {
        return period != null && period.getBegin() != null && period.getEnd() != null;
    }
}
