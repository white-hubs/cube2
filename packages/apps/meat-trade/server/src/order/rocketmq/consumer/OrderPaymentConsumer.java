package com.guhecloud.cube.biz.meattrade.server.order.rocketmq.consumer;

import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.rocketmq.message.OrderPaymentMessage;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderDetailService;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import com.guhecloud.cube.biz.meattrade.server.order.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lzy
 * @date 2021/9/15
 */
@Component("meattrade-orderPaymentConsumer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "meattrade-order-payment", topic = OrderPaymentMessage.TOPIC)
public class OrderPaymentConsumer implements RocketMQListener<OrderPaymentMessage> {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final SettlementService settlementService;

    @Override
    public void onMessage(OrderPaymentMessage orderPaymentMessage) {
        // 获取订单
        Order order = this.orderService.getById(orderPaymentMessage.getOrderId());
        // 获取订单详情
        List<OrderDetail> orderDetails = this.orderDetailService.getByOrderId(orderPaymentMessage.getOrderId());
        // 新增结算记录
        this.settlementService.saveByOrderAndDetails(order, orderDetails);
        //生成溯源信息
        this.orderService.saveSpotCheckResource(orderDetails, order);
        // 发送消息
        this.orderService.sendOrderMsg(order);
    }
}
