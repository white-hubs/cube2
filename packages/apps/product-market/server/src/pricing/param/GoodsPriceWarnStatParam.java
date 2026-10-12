package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 预警统计
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnStatParam extends Paging {
    private Period period;
}