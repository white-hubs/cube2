package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import com.guhecloud.cube.biz.wallet.client.enumeration.RechargeMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * 账户充值参数
 *
 * @author Woody
 * @date 2021/9/10
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeParam {
    /**
     * 充值方式
     */
    @NotNull(message = "充值方式不能为空")
    private RechargeMode mode;

    /**
     * 充值金额
     */
    @NotNull(message = "充值金额不能为空")
    @Min(value = 1, message = "充值金额不能小于1")
    private BigDecimal amount;

    /**
     * 市场租户编号
     */
    private Integer tenant;

    /**
     * 商户ID
     */
    private Long supplierId;
}
