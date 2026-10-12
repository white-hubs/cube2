package com.guhecloud.cube.biz.meattrade.server.order.vo;

import com.guhecloud.cube.biz.meattrade.server.order.enumeration.OrderEnum;
import com.guhecloud.cube.biz.wallet.client.enumeration.ChannelType;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/8
 */
@Data
public class OrderVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单号
     */
    private String code;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 批发商名称
     */
    private String supplierName;
    /**
     * 商户编号
     */
    private String supplierCode;
    /**
     * 商户电话号码
     */
    private String supplierPhone;
    /**
     * 客户主键
     */
    private Long customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户编号
     */
    private String customerPhone;
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
    private ChannelType way;

    /**
     * 订单佣金
     */
    @Decimal
    private BigDecimal commission;

    /**
     * 总数
     */
    private Integer totalSlices;

    /**
     * 付款码
     */
    private String qrcode;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 订单详情信息
     */
    private List<OrderDetailVO> details;

}
