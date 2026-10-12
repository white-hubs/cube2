package com.guhecloud.cube.biz.meattrade.server.order.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 订单结算参数
 *
 * @author Woody
 * @date 2021/9/15
 */
@Data
public class OrderSettleParam {
    /**
     * 支付密码
     */
    @NotEmpty(message = "支付密码不能为空")
    @Size(max = 100, message = "支付密码不能大于100")
    private String password;

}
