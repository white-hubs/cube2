package com.guhecloud.cube.biz.meattrade.server.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商户
 *
 * @author lanchun
 * @date 2023/8/21 14:29
 */
@Data
public class SupplierOrderTradeStatisticsVO {

    /**
     * 商户主键
     */
    private Long supplierId;

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
     * 交易片数
     */
    private Integer slices;
}
