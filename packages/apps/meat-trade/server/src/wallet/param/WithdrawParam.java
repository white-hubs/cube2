package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * 账户提现参数
 *
 * @author Woody
 * @date 2021/8/23
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawParam {
    /**
     * 提现银行卡主键
     */
    @NotNull(message = "提现银行卡主键不能为空")
    @Min(value = 1, message = "提现银行卡主键不能小于1")
    private Long bankCardId;

    /**
     * 提现金额
     */
    @NotNull(message = "充值金额不能为空")
    @Min(value = 1, message = "充值金额不能小于1")
    private BigDecimal amount;

    /**
     * 支付密码
     */
    @NotEmpty(message = "支付密码不能为空")
    @Size(max = 100, message = "支付密码长度不能大于100")
    private String password;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
