package com.guhecloud.cube.biz.daily.task.status.base;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/10 16:57
 * @Description:
 */
public interface StateCondition {

    boolean evaluate(TransitionContext context);
}
