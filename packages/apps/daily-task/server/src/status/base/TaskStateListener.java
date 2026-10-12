package com.guhecloud.cube.biz.daily.task.status.base;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/11 14:04
 * @Description: 任务状态变更全局监听器
 */
public interface TaskStateListener {

    void beforeTransition(TransitionContext context);

    void afterTransition(TransitionContext context);
}
