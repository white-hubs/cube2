package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.WithdrawStatus;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import com.guhecloud.platform.base.standard.jackson.annotation.Desensitize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户提现信息
 *
 * @author Woody
 * @date 2021/9/13
 */
@Data
public class WithdrawVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 提现金额
     */
    @Decimal
    private BigDecimal amount;

    /**
     * 提现银行卡
     */
    private BankCard bankCard;

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

    @Data
    public static class BankCard {
        /**
         * 银行卡名称
         */
        private String name;

        /**
         * 持卡人姓名
         */
        @Desensitize("*?-")
        private String owner;
    }
}
