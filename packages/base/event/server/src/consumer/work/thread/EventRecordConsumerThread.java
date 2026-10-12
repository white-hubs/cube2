package com.guhecloud.cube.base.event.server.consumer.work.thread;

import com.guhecloud.cube.base.event.server.consumer.publisher.EventPublisher;
import com.guhecloud.cube.base.event.server.consumer.publisher.message.EventMessage;
import com.guhecloud.cube.base.event.server.consumer.repostiory.entity.EventConsumerRecord;
import com.guhecloud.cube.base.event.server.consumer.repostiory.service.EventConsumerRecordService;
import com.guhecloud.cube.base.event.server.producer.repository.entity.BusinessEventRecord;
import com.guhecloud.cube.base.event.server.producer.repository.service.BusinessEventRecordService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EventRecordConsumerThread extends Thread {

    private volatile boolean toStop = false;

    private static final String LAST_CONSUMER_EVENT_ID = "LAST_CONSUMER_BUSINESS_EVENT_ID";

    private final RedisTemplate<String, Object> redisTemplate;

    private final BusinessEventRecordService businessEventRecordService;

    private final EventConsumerRecordService eventConsumerRecordService;

    private final EventPublisher eventPublisher;

    public EventRecordConsumerThread(RedisTemplate<String, Object> redisTemplate, BusinessEventRecordService businessEventRecordService,
                                     EventConsumerRecordService eventConsumerRecordService, EventPublisher eventPublisher
                                    ) {
        this.redisTemplate = redisTemplate;
        this.businessEventRecordService = businessEventRecordService;
        this.eventConsumerRecordService = eventConsumerRecordService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void run() {
        while (!toStop) {
            try {
                TimeUnit.MILLISECONDS.sleep(3000 - System.currentTimeMillis() % 1000);
            } catch (InterruptedException e) {
                if (!toStop) {
                    log.error(e.getMessage(), e);
                }
            }
            try {
                Long lastConsumerId = null;
                Object o = this.redisTemplate.opsForValue().get(LAST_CONSUMER_EVENT_ID);
                if (Objects.nonNull(o)) {
                    lastConsumerId = (Long) o;
                } else {
                    //获取消费的最后一条事件
                    lastConsumerId = this.eventConsumerRecordService.getLastEventId();
                }
                List<BusinessEventRecord> businessEventRecords =
                        this.businessEventRecordService.getBusinessEventRecords(100, lastConsumerId);
                if (CollectionUtils.isEmpty(businessEventRecords)) {
                    continue;
                }
                List<EventConsumerRecord> eventConsumerRecords = new ArrayList<>();
                for (BusinessEventRecord businessEventRecord : businessEventRecords) {
                    eventConsumerRecords.add(EventConsumerRecord.builder()
                                                     .eventId(businessEventRecord.getId())
                                                     .timestamp(businessEventRecord.getTimestamp())
                                                     .build());
                    eventPublisher.publish(businessEventRecord.getTopic(), convertToMessage(businessEventRecord));
                }
                eventConsumerRecordService.saveBatch(eventConsumerRecords);
                this.redisTemplate.opsForValue().set(LAST_CONSUMER_EVENT_ID, businessEventRecords.get(0).getId());
            } catch (Exception e) {
                if (!toStop) {
                    log.error("event_record_consumer_error", e);
                }
            }
        }
    }

    public void toStop() {
        this.toStop = true;
    }

    private EventMessage convertToMessage(BusinessEventRecord businessEventRecord) {
        return EventMessage.builder()
                .id(businessEventRecord.getId())
                .data(businessEventRecord.getData())
                .businessEventType(businessEventRecord.getBusinessEventType())
                .timestamp(businessEventRecord.getTimestamp())
                .createTime(businessEventRecord.getCreateTime())
                .build();
    }
}
