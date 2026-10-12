package com.guhecloud.platform.event.src.common;

import com.guhecloud.platform.event.src.entity.EventIdentity;
import com.guhecloud.platform.event.src.enumeration.InnerEventEnum;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InnerEvent {

    public static final List<EventIdentity> EVENT_BUS_MAP = new CopyOnWriteArrayList<>();

    static {
        //内部业务操作事件
        EVENT_BUS_MAP.add(new EventIdentity(InnerEventEnum.PLATFORM_INNER.getName(), InnerEventEnum.PLATFORM_INNER.getPublisherType()));
    }

    private InnerEvent() {}
}
