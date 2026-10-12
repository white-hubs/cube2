package com.guhecloud.cube.srv.gen.device.supervise.server.account.event.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.DeviceLifeEventsCommandService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.event.message.DeviceLifeEventDelMessage;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = DeviceLifeEventDelMessage.TOPIC, topic = DeviceLifeEventDelMessage.TOPIC,
                         selectorType = SelectorType.TAG, messageModel = MessageModel.BROADCASTING
)
public class DeviceLifeEventDelMessageConsumer implements RocketMQListener<DeviceLifeEventDelMessage> {

    private final DeviceLifeEventsCommandService deviceLifeEventsCommandService;

    @Override
    public void onMessage(DeviceLifeEventDelMessage message) {
        deviceLifeEventsCommandService.delEvent(message);
    }
}
