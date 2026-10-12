package com.guhecloud.cube.biz.meattrade.server.order.controller;

import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.enumeration.OrderEnum;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderPayParam;
import com.guhecloud.cube.biz.meattrade.server.order.rocketmq.message.OrderPaymentMessage;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import com.guhecloud.cube.biz.meattrade.server.order.service.PaymentService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 支付接口
 *
 * @author Woody
 * @date 2021/9/15
 */
@Validated
@RestController("meattrade-paymentController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    /**
     * 订单支付
     *
     * @param orderId 订单主键
     * @param param   支付参数
     *
     * @return 支付主键
     */
    @PostMapping("/order/{orderId}/payment")
    public Long payment(@PathVariable("orderId") @Min(value = 1, message = "订单主键不能小于1") Long orderId,
                        @RequestBody @Valid OrderPayParam param
                       ) {
        // 验证订单有效性
        Order order = this.orderService.getById(orderId);
        Assert.isNotFound(order, "订单不存在");
        Assert.isNotAcceptable(order.getStatus() == OrderEnum.CANCELED, "订单已取消");
        Assert.isNotAcceptable(order.getStatus() == OrderEnum.FINISHED, "订单已支付");

        // 发送订单支付事务消息
        Long id = RedisUtils.generateIdentity();
        Long sourceId = null;
        Assert.isNotAcceptable(!Objects.equals(order.getCustomerId(), SecurityContextHolder.getPrincipal().getId()), "订单无效!");
        OrderPaymentMessage message = OrderPaymentMessage.builder().orderId(orderId).paymentId(id).build();
        RocketMQContextHolder.sendTransactionalMessage(OrderPaymentMessage.TOPIC, message, () -> {
            // 执行本地订单支付事务方法
            boolean successful = this.paymentService.payment(id, sourceId, order, param);
            return successful ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
        });
        return id;
    }

}

