package com.guhecloud.cube.biz.meattrade.server.order.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 订单支付参数
 *
 * @author Woody
 * @date 2021/9/15
 */
@Data
public class OrderPayParam {
    /**
     * 支付密码
     */
    @Size(max = 100, message = "支付密码不能大于100")
    private String password;
}