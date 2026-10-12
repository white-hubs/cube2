package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 采价预警记录
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnRecordQueryParam extends Paging {

    //预警名称
    private String ruleName;
    //采价员姓名
    private String userName;
    //商品品类名称
    private String goodsCategoryName;
    //价格是否异常
    private Boolean priceExp;
    //处理人
    private String handler;
    //采价时间段
    private Period period;
    //处理状态
    private Boolean status;

}