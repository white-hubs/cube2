package com.guhecloud.platform.event.src.guava;

import com.google.common.eventbus.EventBus;
import com.guhecloud.platform.event.src.EventPublisher;
import com.guhecloud.platform.event.src.entity.EventIdentity;
import com.guhecloud.platform.event.src.guava.utils.GuavaEventBusUtils;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuavaEventPublisher implements EventPublisher {

    private final Map<EventIdentity, EventBus> eventBusMap;

    public GuavaEventPublisher() {
        this.eventBusMap = new ConcurrentHashMap<>();
    }

    public GuavaEventPublisher(Map<EventIdentity, EventBus> eventBusMap) {
        this.eventBusMap = eventBusMap;
    }

    @Override
    public void register(EventIdentity identity, Object subscriber) {
        var eventBus = findEventBus(identity);
        eventBus.register(subscriber);
    }

    @Override
    public void unregister(EventIdentity identity, Object subscriber) {
        var eventBus = findEventBus(identity);
        eventBus.unregister(subscriber);
    }

    @Override
    public void publish(EventIdentity identity, Object event) {
        var eventBus = findEventBus(identity);
        eventBus.post(event);
    }

    @Override
    public void transactionPublish(EventIdentity identity, Object event) {
        var eventBus = findEventBus(identity);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // 当前存在事务：注册事务同步器，事务提交后执行发布
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    eventBus.post(event); // 事务提交后发布事件
                }
            });
        } else {
            eventBus.post(event);
        }
    }

    private EventBus findEventBus(EventIdentity identity) {
        var eventBus = eventBusMap.get(identity);
        if (eventBus == null) {
            eventBus = createEventBus(identity);
            eventBusMap.put(identity, eventBus);
        }
        return eventBus;
    }

    private EventBus createEventBus(EventIdentity identity) {
        return GuavaEventBusUtils.createEventBus(identity);
    }
}
