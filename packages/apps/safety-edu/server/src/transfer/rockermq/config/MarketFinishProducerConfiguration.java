package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.config;

import com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.producer.wapper.MarketTaskFinishProducerWarp;
import com.guhecloud.platform.base.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.UtilAll;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 市场配置的集团MQ生产者
 */
@Slf4j
@Configuration
@ConditionalOnBean(name = "transferRocketMQProperties")
public class MarketFinishProducerConfiguration {

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public MarketTaskFinishProducerWarp getMarketTaskFinishProducer(TransferRocketMQProperties transferRocketMQProperties) {
        TransferRocketMQProperties.RocketMQConfig rocketMQConfig = transferRocketMQProperties.getRocketMQConfig();
        log.info("初始化RocketMQ生产者信息:{}", rocketMQConfig.getIpAddress() + ":" + rocketMQConfig.getPort());
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(rocketMQConfig.getProducerGroup());
        defaultMQProducer.setRetryTimesWhenSendFailed(rocketMQConfig.getFailRetryTimes());
        defaultMQProducer.setInstanceName("marketPush" + UtilAll.getPid());
        defaultMQProducer.setUnitName(RandomUtils.randomNumbers(13));
        defaultMQProducer.setNamesrvAddr(rocketMQConfig.getIpAddress() + ":" + rocketMQConfig.getPort());
        return new MarketTaskFinishProducerWarp(defaultMQProducer);
    }
}
