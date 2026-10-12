package com.guhecloud.cube.srv.gen.device.supervise.server.account.event.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.DeviceLifeEventsCommandService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.event.message.DeviceLifeEventMessage;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = DeviceLifeEventMessage.TOPIC, topic = DeviceLifeEventMessage.TOPIC,
                         selectorType = SelectorType.TAG
)
public class DeviceLifeEventMessageConsumer implements RocketMQListener<DeviceLifeEventMessage> {

    private final DeviceLifeEventsCommandService deviceLifeEventsCommandService;

    @Override
    public void onMessage(DeviceLifeEventMessage message) {
        deviceLifeEventsCommandService.saveOrUpdateEvents(message);
    }
}
