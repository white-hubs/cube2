package com.guhecloud.cube.biz.meattrade.server.wallet.rocketmq.consumer;

import com.guhecloud.cube.biz.meattrade.server.wallet.service.WithdrawService;
import com.guhecloud.cube.biz.wallet.client.message.WithdrawMessage;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商户提现消息消费者
 *
 * @author Woody
 * @date 2021/9/10
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "meattrade-account-withdraw", topic = WithdrawMessage.TOPIC)
public class AccountWithdrawConsumer implements RocketMQListener<WithdrawMessage> {
    private final WithdrawService withdrawService;

    @Override
    public void onMessage(WithdrawMessage message) {
        this.withdrawService.consumeMessage(message);
    }
}
