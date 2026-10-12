package com.guhecloud.cube.biz.meattrade.server.order.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2021/12/9
 */
@Data
public class SettlementVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 商家名称
     */
    private String supplierName;

    /**
     * 商家编号
     */
    private String supplierCode;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 结算编号
     */
    private String settlementCode;

    /**
     * 账户主键
     */
    private Long accountId;

    /**
     * 交易金额
     */
    @Decimal
    private BigDecimal amount;

    /**
     * 佣金
     */
    @Decimal
    private BigDecimal commission;

    /**
     * 实际金额
     */
    @Decimal
    private BigDecimal actualAmount;

    /**
     * 发起时间
     */
    private LocalDateTime initiateTime;

    /**
     * 结算时间
     */
    private LocalDateTime settlementTime;

    /**
     * 结算状态
     */
    private Boolean status;

}
