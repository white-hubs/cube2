package com.guhecloud.cube.biz.daily.task.status.service.impl;

import com.guhecloud.cube.biz.daily.task.status.base.TaskStateListener;
import com.guhecloud.cube.biz.daily.task.status.base.TransitionContext;
import org.springframework.stereotype.Service;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/12 20:03
 * @Description:
 */
@Service
public class MessageTaskStateListener implements TaskStateListener {
    @Override
    public void beforeTransition(TransitionContext context) {
    }

    @Override
    public void afterTransition(TransitionContext context) {

    }
}
