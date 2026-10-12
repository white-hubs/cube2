package com.guhecloud.cube.biz.meattrade.server.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单销售统计
 *
 * @author lanchun
 * @date 2023/8/21 15:08
 */
@Data
public class OrderTradeStatisticsVO {

    /**
     * 交易重量
     */
    private BigDecimal weight;

    /**
     * 交易佣金
     */
    private BigDecimal commission;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 去除最低限价交易的 总销售额
     */
    private BigDecimal actualAmount;

    /**
     * 去除最低限价交易的 总重量
     */
    private BigDecimal actualWeight;

    /**
     * 交易片数
     */
    private Integer slices;
}
