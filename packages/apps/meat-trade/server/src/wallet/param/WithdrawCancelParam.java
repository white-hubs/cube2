package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Size;

/**
 * 提现取消参数
 *
 * @author Woody
 * @date 2021/9/12
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawCancelParam {
    /**
     * 取消备注
     */
    @Size(max = 200, message = "取消备注长度不能大于200")
    private String remark;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
