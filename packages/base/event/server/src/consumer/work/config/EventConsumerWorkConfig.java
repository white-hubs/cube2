package com.guhecloud.cube.base.event.server.consumer.work.config;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.base.event.server.config.EventProperties;
import com.guhecloud.cube.base.event.server.consumer.publisher.EventPublisher;
import com.guhecloud.cube.base.event.server.consumer.repostiory.service.EventConsumerRecordService;
import com.guhecloud.cube.base.event.server.consumer.work.thread.EventRecordConsumerThread;
import com.guhecloud.cube.base.event.server.producer.repository.service.BusinessEventRecordService;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EventBusListen(order = 1)
public class EventConsumerWorkConfig implements InitializingBean, DisposableBean {

    private EventRecordConsumerThread eventRecordConsumerThread = null;

    @Getter
    private final RedisTemplate<String, Object> redisTemplate;

    @Getter
    private final BusinessEventRecordService businessEventRecordService;

    @Getter
    private final EventConsumerRecordService eventConsumerRecordService;

    @Getter
    private final EventPublisher eventPublisher;

    @Getter
    private final EventProperties eventProperties;

    public EventConsumerWorkConfig(RedisTemplate<String, Object> redisTemplate, BusinessEventRecordService businessEventRecordService,
                                   EventConsumerRecordService eventConsumerRecordService, EventPublisher eventPublisher,
                                   EventProperties eventProperties
                                  ) {
        this.redisTemplate = redisTemplate;
        this.businessEventRecordService = businessEventRecordService;
        this.eventConsumerRecordService = eventConsumerRecordService;
        this.eventPublisher = eventPublisher;
        this.eventProperties = eventProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.nonNull(eventProperties.getConsumer()) && eventProperties.getConsumer()) {
            eventRecordConsumerThread = new EventRecordConsumerThread(getRedisTemplate(),
                                                                      getBusinessEventRecordService(),
                                                                      getEventConsumerRecordService(),
                                                                      getEventPublisher());
            eventRecordConsumerThread.setDaemon(true);
            eventRecordConsumerThread.setName("event,eventRecordConsumerThread");
        }
    }

    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(eventRecordConsumerThread)) {
            eventRecordConsumerThread.toStop();
            stopThread(eventRecordConsumerThread);
        }
    }

    private void stopThread(Thread thread) {
        try {
            TimeUnit.SECONDS.sleep(1);  // wait
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        if (thread.getState() != Thread.State.TERMINATED) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    private void initStart(SysReadEvent event) {
        if (Objects.nonNull(eventRecordConsumerThread)) {
            eventRecordConsumerThread.start();
        }
    }
}
