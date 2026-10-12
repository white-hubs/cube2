package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 采价预警记录
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnRecordParam {

    //最高价
    private BigDecimal maxPrice;
    //中间价
    private BigDecimal midPrice;
    //最低价
    private BigDecimal minPrice;
    //价格是否异常
    private Boolean priceExp;
    //备注
    private String remark;

}