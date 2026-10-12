package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 采价预警-价格修改日志
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnRecordLogParam {

    //预警记录ID
    private Long warnId;
    //处理人ID
    private Long handlerId;
    //处理人姓名
    private String handlerName;
    //原始最高价
    private BigDecimal oldMaxPrice;
    //原始中间价
    private BigDecimal oldMidPrice;
    //原始最低价
    private BigDecimal oldMinPrice;
    //修改最高价
    private BigDecimal newMaxPrice;
    //修改中间价
    private BigDecimal newMidPrice;
    //修改对低价
    private BigDecimal newMinPrice;

}