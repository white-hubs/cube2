package com.guhecloud.cube.base.event.server.consumer.publisher;

public interface EventPublisher {

    void publish(String topic, Object event);
}
