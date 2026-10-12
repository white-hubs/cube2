package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.mq.consumer;

import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.mq.message.JsCloudParkInDelayMessage;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.service.JsCloudParkingService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 捷顺云-入场记录延时处理消费者
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "jscloud_park_in_delay_group", topic = JsCloudParkInDelayMessage.TOPIC)
public class JsCloudParkInDelayConsumer implements RocketMQListener<JsCloudParkInDelayMessage> {

    private final JsCloudParkingService jsCloudParkingService;

    @Override
    public void onMessage(JsCloudParkInDelayMessage message) {
        jsCloudParkingService.handleParkingInRecord(message.getDataItems(), message.getTenant());
    }
}

