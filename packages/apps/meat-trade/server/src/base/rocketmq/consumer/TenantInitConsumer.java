package com.guhecloud.cube.biz.meattrade.server.base.rocketmq.consumer;

import com.guhecloud.cube.biz.meattrade.server.base.service.DictionaryService;
import com.guhecloud.cube.biz.ouaa.client.message.TenantInitMessage;
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
@RocketMQMessageListener(consumerGroup = TenantInitMessage.TOPIC + "_" + "MEATTRADE", topic = TenantInitMessage.TOPIC)
public class TenantInitConsumer implements RocketMQListener<TenantInitMessage> {

    private final DictionaryService dictionaryService;

    @Override
    public void onMessage(TenantInitMessage message) {
        // 初始化食安抽检项信息
        this.dictionaryService.initSpotCheckItem(message.getTenant());
    }
}
