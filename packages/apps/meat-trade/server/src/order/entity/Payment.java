package com.guhecloud.cube.biz.meattrade.server.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.wallet.client.enumeration.ChannelType;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * 订单支付数据模型
 *
 * @author Woody
 * @date 2021/9/15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("meattrade_payment")
public class Payment extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 支付方式
     */
    private ChannelType mode;

    /**
     * 订单主键
     */
    private Long orderId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 租户编号
     */
    private Integer tenant;
}
