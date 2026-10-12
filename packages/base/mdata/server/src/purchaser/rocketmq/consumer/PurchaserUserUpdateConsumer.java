package com.guhecloud.cube.biz.mdata.server.purchaser.rocketmq.consumer;

import com.guhecloud.cube.biz.mdata.server.purchaser.service.PurchaserService;
import com.guhecloud.cube.biz.ouaa.client.message.UserUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "purchaser_" + UserUpdateMessage.TOPIC,
                         topic = UserUpdateMessage.TOPIC
)
public class PurchaserUserUpdateConsumer implements RocketMQListener<UserUpdateMessage> {

    private final PurchaserService purchaserService;

    @Override
    public void onMessage(UserUpdateMessage message) {
        this.purchaserService.updatePurchaserName(message.getId(), message.getName());
    }
}
