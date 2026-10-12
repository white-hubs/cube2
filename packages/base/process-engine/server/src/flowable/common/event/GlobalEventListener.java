package com.guhecloud.cube.base.flow.flowable.common.event;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.springframework.stereotype.Component;

/**
 * @author pengjian
 * @date 2022/9/7 17:05
 * @description
 */
@Slf4j
@Component
public class GlobalEventListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent event) {
        if ((event.getType() == ActivitiEventType.PROCESS_COMPLETED) || (event.getType() == ActivitiEventType.ACTIVITY_COMPLETED)) {
            // 分发
            EventDispatcher.getInstance().fireEvent(event);
        }
    }

    @Override
    public boolean isFailOnException() {
        return true;
    }
}
