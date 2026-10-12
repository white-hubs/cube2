package com.guhecloud.cube.biz.meattrade.server.order.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单支付消息
 *
 * @author Woody
 * @date 2021/9/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentMessage {
    /**
     * 消息主题
     */
    public static final String TOPIC = "order-payment";

    /**
     * 订单主键
     */
    private Long orderId;

    /**
     * 支付主键
     */
    private Long paymentId;
}
