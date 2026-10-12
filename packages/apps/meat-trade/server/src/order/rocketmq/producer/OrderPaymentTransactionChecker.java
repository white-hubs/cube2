package com.guhecloud.cube.biz.meattrade.server.order.rocketmq.producer;

import com.guhecloud.cube.biz.meattrade.server.order.entity.Payment;
import com.guhecloud.cube.biz.meattrade.server.order.rocketmq.message.OrderPaymentMessage;
import com.guhecloud.cube.biz.meattrade.server.order.service.PaymentService;
import com.guhecloud.platform.rocketmq.producer.RocketMQTransactionChecker;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 订单支付本地事务消息检测实现
 *
 * @author Woody
 * @date 2021/9/6
 */
@Component("meattrade-orderPaymentTransactionChecker")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderPaymentTransactionChecker implements RocketMQTransactionChecker<OrderPaymentMessage> {
    private final PaymentService paymentService;

    @Override
    public String getTopic() {
        return OrderPaymentMessage.TOPIC;
    }

    @Override
    public Class<OrderPaymentMessage> getPayloadClass() {
        return OrderPaymentMessage.class;
    }

    @Override
    public RocketMQLocalTransactionState check(Message<?> message, OrderPaymentMessage payload) {
        Payment payment = this.paymentService.getById(payload.getPaymentId());
        return payment == null ? RocketMQLocalTransactionState.ROLLBACK : RocketMQLocalTransactionState.COMMIT;
    }
}
