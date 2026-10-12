package com.guhecloud.platform.event.src.guava.utils;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.platform.event.src.entity.EventIdentity;
import com.guhecloud.platform.event.src.enumeration.PublisherTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GuavaEventBusUtils {

    private static final Executor EXECUTOR = new ThreadPoolExecutor(20, 100, 60,
                                                                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(Short.MAX_VALUE), new ThreadFactoryBuilder()
                                                                            .setNameFormat("guava-common-pool-%d")
                                                                            .build(), rejectedExecutionHandler());

    private static RejectedExecutionHandler rejectedExecutionHandler() {
        return (r, executor) -> log.warn("{} Event rejected", "async_event_bus");
    }

    private GuavaEventBusUtils() {

    }

    public static EventBus createEventBus(EventIdentity identity, Executor executor) {
        if (identity.getPublisherType() == PublisherTypeEnum.ASYNC) {
            return new AsyncEventBus(identity.getEventKey(), executor);
        }
        return new EventBus(identity.getEventKey());
    }

    public static EventBus createEventBus(EventIdentity identity) {
        return createEventBus(identity, EXECUTOR);
    }
}
