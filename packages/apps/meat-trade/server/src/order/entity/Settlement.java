package com.guhecloud.cube.biz.meattrade.server.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 结算信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("meattrade_settlement")
public class Settlement extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 结算编号
     */
    private String settlementCode;

    /**
     * 订单号
     */
    private String orderCode;

    /**
     * 账户主键
     */
    private Long accountId;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易佣金
     */
    private BigDecimal commission;

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

    /**
     * 市场方租户编号
     */
    private Integer tenant;
}
