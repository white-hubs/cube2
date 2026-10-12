package com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 光明rocketMQ推送配置
 *
 * @author lanchun
 * @date 2023/4/13 11:59
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "guangming.meattrade.arrange")
@ConditionalOnProperty(prefix = "guangming.meattrade.arrange.rocketmq-config", value = "ipAddress")
public class GuangMingRocketMQProperties {

    private final RocketMQConfig rocketMQConfig = new RocketMQConfig();

    /**
     * RocketMQ配置
     */
    @Data
    public static class RocketMQConfig {

        /**
         * ip地址
         */
        private String ipAddress;

        /**
         * 端口
         */
        private String port;

        /**
         * 授权码
         */
        private String appKey;

        /**
         * 生产组
         */
        private String producerGroup;

        /**
         * 理货数据推送mq配置
         */
        private TallyPushMQConfig tallyPushProducer;

        /**
         * 挂钩交易完成消息消费配置
         */
        private CoupleTradeCompletedMQConfig coupleTradeCompletedConsumer;

        /**
         * 发送失败的重试次数
         */
        private Integer failRetryTimes;
    }

    @Data
    public static class TallyPushMQConfig {

        /**
         * 消息主题
         */
        private String topic;
    }

    @Data
    public static class CoupleTradeCompletedMQConfig {

        /**
         * 消息主题
         */
        private String topic;

        /**
         * 消费组
         */
        private String group;

        /**
         * 消费tag
         */
        private String tag;
    }
}
