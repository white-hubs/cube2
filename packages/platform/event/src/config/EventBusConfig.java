package com.guhecloud.platform.event.src.config;

import com.google.common.eventbus.EventBus;
import com.guhecloud.platform.event.src.EventPublisher;
import com.guhecloud.platform.event.src.common.InnerEvent;
import com.guhecloud.platform.event.src.entity.EventIdentity;
import com.guhecloud.platform.event.src.guava.GuavaEventPublisher;
import com.guhecloud.platform.event.src.guava.utils.GuavaEventBusUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class EventBusConfig {

    @Bean("eventPublisher")
    public EventPublisher eventBus() {
        Map<EventIdentity, EventBus> eventBusMap = new ConcurrentHashMap<>();
        for (EventIdentity eventIdentity : InnerEvent.EVENT_BUS_MAP) {
            eventBusMap.put(eventIdentity, GuavaEventBusUtils.createEventBus(eventIdentity));
        }
        return new GuavaEventPublisher(eventBusMap);
    }
}
