package com.guhecloud.cube.base.event.server.producer.rocketmq.consumer;

import com.guhecloud.cube.base.event.server.config.EventProperties;
import com.guhecloud.cube.base.event.server.producer.repository.entity.BusinessEventRecord;
import com.guhecloud.cube.base.event.server.producer.repository.service.BusinessEventRecordService;
import com.guhecloud.cube.base.event.server.producer.rocketmq.message.BusinessEventMessage;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "BUSINESS_EVENT", topic = BusinessEventMessage.TOPIC, selectorType = SelectorType.TAG)
public class BusinessEventConsumer implements RocketMQListener<BusinessEventMessage> {

    private final BusinessEventRecordService businessEventRecordService;

    private final EventProperties eventProperties;

    @Override
    public void onMessage(BusinessEventMessage message) {
        if (Objects.nonNull(eventProperties.getProducer()) &&
            eventProperties.getProducer()) {
            BusinessEventRecord businessEventRecord = ObjectUtils.copying(message, BusinessEventRecord.class);
            businessEventRecord.setCreateTime(DateUtils.now());
            businessEventRecordService.save(businessEventRecord);
        }
    }
}
