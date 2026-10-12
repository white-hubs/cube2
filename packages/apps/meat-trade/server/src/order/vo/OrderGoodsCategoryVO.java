package com.guhecloud.cube.biz.meattrade.server.order.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品品类信息
 *
 * @author lanchun
 * @date 2023/8/16 10:24
 */
@Data
public class OrderGoodsCategoryVO {

    /**
     * 订单主键
     */
    private Long orderId;

    /**
     * 商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 总数
     */
    private int total;

    /**
     * 总金额
     */
    @Decimal
    private BigDecimal amount;
}
