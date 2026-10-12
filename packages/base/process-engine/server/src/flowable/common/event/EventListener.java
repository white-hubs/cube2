package com.guhecloud.cube.base.flow.flowable.common.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/15 9:55
 * @description
 */
public abstract class EventListener {
    /**
     * 在初始化listener的同时注册listener
     */
    public EventListener() {
    }

    /**
     * 感兴趣的事件列表
     * event 请看{@link ActivitiEventType}
     *
     * @return
     */
    public abstract List<ActivitiEventType> interest();

    /**
     * 处理事件
     *
     * @param event
     */
    public abstract void onEvent(ActivitiEvent event);
}
