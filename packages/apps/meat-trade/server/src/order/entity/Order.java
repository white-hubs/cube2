package com.guhecloud.cube.biz.meattrade.server.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.order.enumeration.OrderEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单信息
 *
 * @author lzy
 * @date 2021/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("meattrade_order")
public class Order extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单号
     */
    private String code;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 买家主键
     */
    private Long customerId;

    /**
     * 总金额
     */
    @Decimal
    private BigDecimal amount;

    /**
     * 商品总重量
     */
    @Decimal
    private BigDecimal weight;

    /**
     * 商品总损耗
     */
    @Decimal
    private BigDecimal loss;

    /**
     * 实际总重量
     */
    @Decimal
    private BigDecimal actualWeight;

    /**
     * 总佣金
     */
    @Decimal
    private BigDecimal commission;

    /**
     * 下单时间
     */
    private LocalDateTime orderTime;

    /**
     * 订单状态
     */
    private OrderEnum status;

    /**
     * 支付方式
     */
    private String way;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
