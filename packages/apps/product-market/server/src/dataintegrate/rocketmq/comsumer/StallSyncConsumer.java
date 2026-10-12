package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.rocketmq.comsumer;

import com.guhecloud.cube.srv.gen.product.market.server.common.dao.MarketStallMapper;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.rocketmq.message.StallSyncMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

/**
 * 监听业务操作日志
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = StallSyncMessage.TOPIC + "_" + "PRODUCT", topic = StallSyncMessage.TOPIC)
public class StallSyncConsumer implements RocketMQListener<StallSyncMessage> {
    private final MarketStallMapper marketStallMapper;

    @Override
    public void onMessage(StallSyncMessage message) {
        log.info("档位更新日志：{}", message);
        if (!"DEL".equals(message.getOptFlag())) {
            marketStallMapper.insertOrUpdateStallBatch(message.getStallList());
        } else {
            Optional.ofNullable(message.getStallList()).orElse(Collections.emptyList()).stream()
                    .forEach(d -> marketStallMapper.deleteById(d.getId()));
        }
    }
}
