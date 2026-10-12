package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.config;

import com.guhecloud.cube.srv.gen.safety.education.server.transfer.constant.TransferTopicConstant;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.consumer.MarketTaskConsumer;
import com.guhecloud.platform.base.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置集团的消费者信息
 */
@Slf4j
@Configuration
@ConditionalOnBean(name = "transferRocketMQProperties")
public class GroupTrainingTaskConfig {

    @Bean(initMethod = "start", destroyMethod = "shutdown", name = "GroupTaskConsumer")
    public DefaultMQPushConsumer createGroupTaskConsumer(TransferRocketMQProperties transferRocketMQProperties,
                                                         MarketTaskConsumer orderly
                                                        ) throws MQClientException {
        TransferRocketMQProperties.RocketMQConfig rocketMQConfig = transferRocketMQProperties.getRocketMQConfig();
        log.info("初始化安全教育RocketMQ消费者信息:{}", rocketMQConfig.getIpAddress() + ":" + rocketMQConfig.getPort());
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(transferRocketMQProperties
                                                                                        .getRocketMQConfig().getMarketReceiveGroup());
        defaultMQPushConsumer.setInstanceName("group" + UtilAll.getPid());
        defaultMQPushConsumer.setUnitName(RandomUtils.randomNumbers(13));
        defaultMQPushConsumer.setNamesrvAddr(rocketMQConfig.getIpAddress() + ":" + rocketMQConfig.getPort());
        //注册集团发布任务topic
        defaultMQPushConsumer.subscribe(TransferTopicConstant.CULTIVATE_TASK_TOPIC, "*");
        defaultMQPushConsumer.subscribe(TransferTopicConstant.EXAM_TASK_TOPIC, "*");

        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        // 设置消息监听器为有序消费，默认并行消费
        defaultMQPushConsumer.setMessageListener(orderly);
        return defaultMQPushConsumer;
    }
}
