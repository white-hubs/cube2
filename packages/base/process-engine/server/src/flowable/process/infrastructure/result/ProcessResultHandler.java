package com.guhecloud.cube.base.flow.flowable.process.infrastructure.result;

import com.guhecloud.cube.base.flow.flowable.process.domain.ProcessInstanceContext;
import org.activiti.engine.delegate.event.ActivitiEventType;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/15 11:05
 * @description
 */
public abstract class ProcessResultHandler {
    /**
     * 在初始化listener的同时注册listener
     */
    public ProcessResultHandler() {
    }

    /**
     * 感兴趣的事件列表
     * event 请看{@link ActivitiEventType}
     *
     * @return
     */
    abstract public List<String> interest();

    /**
     * 处理事件
     *
     * @param processInstanceContext
     */
    abstract public void onEvent(ProcessInstanceContext processInstanceContext);
}
