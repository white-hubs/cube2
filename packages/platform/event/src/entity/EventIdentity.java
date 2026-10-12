package com.guhecloud.platform.event.src.entity;

import com.guhecloud.platform.event.src.enumeration.InnerEventEnum;
import com.guhecloud.platform.event.src.enumeration.PublisherTypeEnum;
import lombok.Getter;

import java.util.Objects;

@Getter
public class EventIdentity {

    /**
     * 事件key
     */
    private final String eventKey;

    /**
     * 发布方式
     */
    private final PublisherTypeEnum publisherType;

    public EventIdentity(String eventKey, PublisherTypeEnum publisherType) {
        this.eventKey = eventKey;
        this.publisherType = publisherType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventIdentity that = (EventIdentity) o;
        return Objects.equals(eventKey, that.eventKey) && publisherType == that.publisherType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventKey, publisherType);
    }

    public static EventIdentity createSysReadEvent() {
        return new EventIdentity(InnerEventEnum.PLATFORM_INNER.getName(), InnerEventEnum.PLATFORM_INNER.getPublisherType());
    }
}
