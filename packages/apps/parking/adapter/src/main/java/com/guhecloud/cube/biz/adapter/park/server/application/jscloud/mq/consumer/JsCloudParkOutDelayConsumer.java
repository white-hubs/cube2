package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.mq.consumer;

import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.mq.message.JsCloudParkOutDelayMessage;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.service.JsCloudParkingService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 捷顺云-出场记录延时处理消费者
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "jscloud_park_out_delay_group", topic = JsCloudParkOutDelayMessage.TOPIC)
public class JsCloudParkOutDelayConsumer implements RocketMQListener<JsCloudParkOutDelayMessage> {

    private final JsCloudParkingService jsCloudParkingService;

    @Override
    public void onMessage(JsCloudParkOutDelayMessage message) {
        jsCloudParkingService.handleParkingOutRecord(message.getDataItems(), message.getTenant());
    }
}

