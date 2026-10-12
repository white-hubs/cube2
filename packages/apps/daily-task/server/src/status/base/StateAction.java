package com.guhecloud.cube.biz.daily.task.status.base;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/10 16:58
 * @Description:
 */
public interface StateAction {

    int getOrder();

    void beforeActionExecute(TransitionContext context);

    void afterActionExecute(TransitionContext context);

    String getBusinessEvent();
}
