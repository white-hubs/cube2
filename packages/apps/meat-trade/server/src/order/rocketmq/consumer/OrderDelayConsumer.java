package com.guhecloud.cube.biz.meattrade.server.order.rocketmq.consumer;

import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.enumeration.OrderEnum;
import com.guhecloud.cube.biz.meattrade.server.order.rocketmq.message.OrderDelayMessage;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lzy
 * @date 2021/12/22
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = OrderDelayMessage.TOPIC + "_" + "MEATTRADE", topic = OrderDelayMessage.TOPIC)
public class OrderDelayConsumer implements RocketMQListener<OrderDelayMessage> {

    private final OrderService orderService;

    @Override
    public void onMessage(OrderDelayMessage message) {
        Order order = this.orderService.getById(message.getOrderId());
        if (order.getStatus() == OrderEnum.WAITED) {
            this.orderService.sendOrderMsg(order);
        }
    }
}
