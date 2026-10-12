package com.guhecloud.cube.biz.meattrade.server.wallet.rocketmq.consumer;

import com.guhecloud.cube.biz.meattrade.server.wallet.service.DepositService;
import com.guhecloud.cube.biz.wallet.client.message.RechargeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商户充值消息消费者
 *
 * @author Woody
 * @date 2021/9/10
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "meattrade-account-recharge", topic = RechargeMessage.TOPIC)
public class AccountRechargeConsumer implements RocketMQListener<RechargeMessage> {
    private final DepositService depositService;

    @Override
    public void onMessage(RechargeMessage message) {
        this.depositService.consumeMessage(message);
    }
}
