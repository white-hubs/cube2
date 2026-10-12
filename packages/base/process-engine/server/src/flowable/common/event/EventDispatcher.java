package com.guhecloud.cube.base.flow.flowable.common.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author pengjian
 * @date 2022/10/14 16:33
 * @description 流程事件分发器
 * <p>
 * 采用单例模式
 */
@SuppressWarnings("EqualsHashCode")
public class EventDispatcher {

    public final Logger log = LoggerFactory.getLogger(EventDispatcher.class);
    /**
     * 并发事件组，实现列表的并发更新
     */
    public CopyOnWriteArrayList<Entry> listenerHub = new CopyOnWriteArrayList<>();

    /**
     * 处理事件
     *
     * @param event
     */
    public void fireEvent(ActivitiEvent event) {
        if (null == event) {
            throw new IllegalArgumentException();
        }
        for (EventListener listener : getEntry(event.getType()).listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                log.error(e.toString(), e);
            }

        }
    }

    /**
     * 注册监听事件
     *
     * @param listener
     */
    public void registerListener(EventListener listener) {
        for (ActivitiEventType eventClass : listener.interest()) {
            getEntry(eventClass).listeners.addIfAbsent(listener);
        }
    }

    /**
     * 清空事件总线
     */
    public void clear() {
        listenerHub.clear();

    }

    /**
     * 获取事件组
     *
     * @param eventClass
     *
     * @return
     */
    private Entry getEntry(ActivitiEventType eventClass) {
        for (; ; ) {
            for (Entry entry : listenerHub) {
                if (entry.eventType == eventClass) {
                    return entry;
                }
            }
            Entry temp = new Entry(eventClass);
            //如果不存在，才添加
            if (listenerHub.addIfAbsent(temp)) {
                return temp;
            }
        }

    }

    /**
     * 按照事件类型，设计一个监听组
     */
    private class Entry {
        final ActivitiEventType eventType;
        CopyOnWriteArrayList<EventListener> listeners;

        Entry(ActivitiEventType type) {
            eventType = type;
            listeners = new CopyOnWriteArrayList<EventListener>();
        }

        @Override
        public boolean equals(Object obj) {
            if (null == obj || (obj.getClass() != this.getClass())) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            return eventType == ((Entry) obj).eventType;
        }

    }

    /**
     * 单例控制 EventDispatcher
     */
    private static class EventDispatcherFactory {
        public static EventDispatcher _instance = new EventDispatcher();
    }

    public static EventDispatcher getInstance() {
        return EventDispatcherFactory._instance;
    }

    private EventDispatcher() {
    }
}
