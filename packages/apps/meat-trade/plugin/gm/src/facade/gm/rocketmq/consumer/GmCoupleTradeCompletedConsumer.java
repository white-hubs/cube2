package com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.consumer;

import com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.config.GuangMingRocketMQProperties;
import com.guhecloud.platform.base.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnBean(name = "guangMingRocketMQProperties")
public class GmCoupleTradeCompletedConsumer {

    @Bean(initMethod = "start", destroyMethod = "shutdown", name = "gmTradeCompletedConsumer")
    public DefaultMQPushConsumer createGmCoupleTradeCompletedConsumer(GuangMingRocketMQProperties guangMingRocketMQProperties,
                                                                      GmCoupleTradeCompletedListenerOrderly orderly
                                                                     ) throws MQClientException {
        GuangMingRocketMQProperties.RocketMQConfig rocketMQConfig = guangMingRocketMQProperties.getRocketMQConfig();
        log.info("初始化光明RocketMQ消费者信息:{}", rocketMQConfig.getIpAddress() + ":" + rocketMQConfig.getPort());
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(guangMingRocketMQProperties
                                                                                        .getRocketMQConfig()
                                                                                        .getCoupleTradeCompletedConsumer()
                                                                                        .getGroup());
        defaultMQPushConsumer.setInstanceName("gm" + UtilAll.getPid());
        defaultMQPushConsumer.setUnitName(RandomUtils.randomNumbers(13));
        defaultMQPushConsumer.setNamesrvAddr(rocketMQConfig.getIpAddress() + ":" + rocketMQConfig.getPort());
        defaultMQPushConsumer.subscribe(rocketMQConfig.getCoupleTradeCompletedConsumer().getTopic(), "*");
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        // 设置消息监听器为有序消费，默认并行消费
        defaultMQPushConsumer.setMessageListener(orderly);
        return defaultMQPushConsumer;
    }
}
