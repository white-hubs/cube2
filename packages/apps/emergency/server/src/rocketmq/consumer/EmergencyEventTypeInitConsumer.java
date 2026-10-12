package com.guhecloud.cube.biz.emergency.server.rocketmq.consumer;

import com.guhecloud.cube.biz.emergency.server.rocketmq.message.TenantInitMessage;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyEventTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "emergency_event_type", topic = TenantInitMessage.TOPIC, selectorType = SelectorType.TAG)
public class EmergencyEventTypeInitConsumer implements RocketMQListener<TenantInitMessage> {

    private final EmergencyEventTypeService emergencyEventTypeService;

    @Override
    public void onMessage(TenantInitMessage message) {
        this.emergencyEventTypeService.initEventTypes(message.getTenant());
    }
}
