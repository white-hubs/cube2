package com.guhecloud.cube.biz.meattrade.server.wallet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.RechargeStatus;
import com.guhecloud.cube.biz.wallet.client.enumeration.RechargeMode;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户充值数据模型
 *
 * @author Woody
 * @date 2021/9/7
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("meattrade_deposit")
public class Deposit extends BasicEnhancedEntity<Long> {
    /**
     * 主键（自定义业务流水号）
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 简短主键（主键后六位）
     */
    private Integer shortId;

    /**
     * 用户主键
     */
    private Long userId;

    private String userName;

    /**
     * 商户主键
     */
    private Long supplierId;

    private String supplierName;

    /**
     * 充值方式
     */
    private RechargeMode mode;

    /**
     * 充值金额
     */
    private BigDecimal amount;

    /**
     * 充值状态
     */
    private RechargeStatus status;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 市场租户编号
     */
    private Integer tenant;

    private String userPhone;

    private String supplierPhone;
}
