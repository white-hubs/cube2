package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "safety.education")
@ConditionalOnProperty(prefix = "safety.education.rocketmq-config", value = "ipAddress")
public class TransferRocketMQProperties {
    private final RocketMQConfig rocketMQConfig = new RocketMQConfig();

    /**
     * 集团RocketMQ配置：填写集团的MQ配置信息
     */
    @Data
    public static class RocketMQConfig {

        /**
         * 集团MQip地址
         */
        private String ipAddress;

        /**
         * 集团MQ端口
         */
        private String port;

        /**
         * 集团MQ授权码
         */
        private String appKey;

        /**
         * 集团生产组
         */
        private String producerGroup;

        /**
         * 市场消息消费配置
         */
        private String marketReceiveGroup;

        /**
         * 发送失败的重试次数
         */
        private Integer failRetryTimes;
    }
}
