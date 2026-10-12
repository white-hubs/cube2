package com.guhecloud.cube.iot.north.bridge.consumer.auto;

import com.guhecloud.cube.iot.north.bridge.consumer.RabbitMqSubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.cfg.RabbitSubscribeConfig;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnProperty(prefix = "thirdparty.iot.subscribe.full.consumer.rabbit", name = "host")
@ConditionalOnMissingBean(SubscribeContainer.class)
public class RabbitSubscribeContainerAutoConfig {

    @Bean
    @ConfigurationProperties(prefix = "thirdparty.iot.subscribe.full.consumer.rabbit")
    public RabbitSubscribeConfig subscribeConfig() {
        return new RabbitSubscribeConfig();
    }

    @Bean
    public ConnectionFactory connectionFactory(RabbitSubscribeConfig rabbitSubscribeConfig) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(rabbitSubscribeConfig.getHost());
        cachingConnectionFactory.setPort(rabbitSubscribeConfig.getPort());
        cachingConnectionFactory.setUsername(rabbitSubscribeConfig.getUsername());
        cachingConnectionFactory.setPassword(rabbitSubscribeConfig.getPassword());
        cachingConnectionFactory.setVirtualHost(rabbitSubscribeConfig.getVirtualHost());
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitMqSubscribeContainer subscribeContainer(ConnectionFactory connectionFactory,
                                                         RabbitSubscribeConfig subscribeConfig) {

        // 设置默认的消费组，如果没有设置的话
        String consumerGroup = subscribeConfig.getConsumerGroup();
        if (StringUtils.isEmpty(consumerGroup)) {
            subscribeConfig.setConsumerGroup("DEFAULT");
        }

        return new RabbitMqSubscribeContainer(connectionFactory, subscribeConfig);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
