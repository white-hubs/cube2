package com.guhecloud.cube.base.flow.flowable.task.application.event.consumer;

import org.activiti.engine.delegate.DelegateTask;

/**
 * @author pengjian
 * @date 2022/9/5 15:17
 * @description
 */
public interface TaskInstanceEventHandler {
    /**
     * 任务实例创建
     *
     * @param delegateTask
     */
    void create(DelegateTask delegateTask);

    /**
     * 用户分配
     *
     * @param delegateTask
     */
    void assignment(DelegateTask delegateTask);

    /**
     * 任务完成
     *
     * @param delegateTask
     */
    void complete(DelegateTask delegateTask);
}
