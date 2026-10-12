package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.WithdrawStatus;
import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotNull;

/**
 * 我的账户提现查询参数
 *
 * @author Woody
 * @date 2021/9/13
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MineWithdrawQueryParam extends Paging {
    /**
     * 提现状态
     */
    @NotNull(message = "提现状态不能为空")
    private WithdrawStatus status;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
