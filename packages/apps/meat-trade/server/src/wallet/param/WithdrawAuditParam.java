package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.WithdrawStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotNull;

/**
 * 账户提现审核参数
 *
 * @author Woody
 * @date 2021/8/24
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawAuditParam {
    /**
     * 审核备注
     */
    private String remark;

    /**
     * 审核状态
     */
    @NotNull(message = "审核状态不能为空")
    private WithdrawStatus status;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
