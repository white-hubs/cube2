package com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.config;

import com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.producer.wapper.GmMeattradeProducerWarp;
import com.guhecloud.platform.base.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.UtilAll;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnBean(name = "guangMingRocketMQProperties")
public class GmTallyPushProducerConfiguration {

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public GmMeattradeProducerWarp getGmMeattradeProducer(GuangMingRocketMQProperties guangMingRocketMQProperties) {
        GuangMingRocketMQProperties.RocketMQConfig rocketMQConfig = guangMingRocketMQProperties.getRocketMQConfig();
        log.info("初始化光明RocketMQ生产者信息:{}", rocketMQConfig.getIpAddress() + ":" + rocketMQConfig.getPort());
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(rocketMQConfig.getProducerGroup());
        defaultMQProducer.setRetryTimesWhenSendFailed(rocketMQConfig.getFailRetryTimes());
        defaultMQProducer.setInstanceName("gm" + UtilAll.getPid());
        defaultMQProducer.setUnitName(RandomUtils.randomNumbers(13));
        defaultMQProducer.setNamesrvAddr(rocketMQConfig.getIpAddress() + ":" + rocketMQConfig.getPort());
        return new GmMeattradeProducerWarp(defaultMQProducer,
                                           rocketMQConfig.getTallyPushProducer().getTopic(),
                                           rocketMQConfig.getAppKey());
    }
}
