package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 食品安全检查
 * @date 2023-12-04 10:08:36
 */
@Data
public class GoodsSafetyCheckQueryParam extends Paging {

    //档位号
    private String stallCode;
    //品类名
    private String goodsCategoryName;
    //产地
    private String goodsFrom;
    //检测人名称
    private String checkResult;
    //检测日期
    private Period period;

}