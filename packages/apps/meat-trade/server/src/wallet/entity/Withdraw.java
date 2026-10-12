package com.guhecloud.cube.biz.meattrade.server.wallet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.WithdrawStatus;
import com.guhecloud.cube.biz.wallet.client.enumeration.BankType;
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
 * 账户提现数据模型
 *
 * @author Woody
 * @date 2021/8/23
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("meattrade_withdraw")
public class Withdraw extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    private String userPhone;

    /**
     * 商户主键
     */
    private Long supplierId;
    private String supplierName;
    private String supplierPhone;

    /**
     * 银行卡信息JSON字符串
     */
    private String bankCardJson;

    /**
     * 提现金额
     */
    private BigDecimal amount;

    /**
     * 申请人
     */
    private String owner;

    /**
     * 审核员
     */
    private String auditor;

    /**
     * 转账员
     */
    private String completer;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 提现审核状态
     */
    private WithdrawStatus status;

    /**
     * 市场租户编号
     */
    private Integer tenant;

    /**
     * 提现银行卡信息
     */
    @Data
    public static class BankCard {
        /**
         * 银行类型
         */
        private BankType bank;

        /**
         * 银行卡号
         */
        private String code;

        /**
         * 持卡人姓名
         */
        private String owner;

        /**
         * 开户行名称
         */
        private String issuer;
    }
}
