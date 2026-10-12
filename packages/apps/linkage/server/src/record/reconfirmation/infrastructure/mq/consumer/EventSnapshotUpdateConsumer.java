package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.mq.consumer;

import com.guhecloud.cube.biz.linkage.server.record.infrastructure.repository.EventRecordRepository;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.application.event.handler.IotReConfirmEventHandler;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.event.message.EventSnapshotUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "LINkAGE_EVENT_SNAPSHOT", topic = EventSnapshotUpdateMessage.TOPIC,
                         selectorType = SelectorType.TAG
)
public class EventSnapshotUpdateConsumer implements RocketMQListener<EventSnapshotUpdateMessage> {

    private final IotReConfirmEventHandler iotReConfirmEventHandler;

    private final EventRecordRepository eventRecordRepository;

    @Override
    public void onMessage(EventSnapshotUpdateMessage message) {
        var eventRecord = this.eventRecordRepository.detailByEventId(message.getEventId());
        if (Objects.nonNull(eventRecord)) {
            iotReConfirmEventHandler.iotReConfirmEvent(eventRecord, message.getSnapshot());
        }
    }
}
