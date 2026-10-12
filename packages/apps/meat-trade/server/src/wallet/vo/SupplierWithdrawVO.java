package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.Withdraw;
import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.WithdrawStatus;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户提现记录信息
 *
 * @author Woody
 * @date 2021/9/13
 */
@Data
public class SupplierWithdrawVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 银行卡信息
     */
    private Withdraw.BankCard bankCard;

    /**
     * 商户信息
     */
    private SupplierDTO supplier;

    /**
     * 提现金额
     */
    @Decimal
    private BigDecimal amount;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 提现审核状态
     */
    private WithdrawStatus status;

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
     * 创建时间
     */
    private LocalDateTime createTime;
}
