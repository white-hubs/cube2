package com.guhecloud.cube.base.flow.flowable.process.application.event.consumer;

import com.guhecloud.cube.base.flow.flowable.common.event.EventListener;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.ProcessInstanceContextBuilder;
import com.guhecloud.cube.base.flow.flowable.process.domain.ProcessInstanceContext;
import com.guhecloud.cube.base.flow.flowable.process.domain.event.ProcessCompleteEvent;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.result.ProcessResultHandleDispatcher;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/15 9:53
 * @description 流程完成事件监听
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessCompletedEventListener extends EventListener {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ProcessInstanceContextBuilder processInstanceContextBuilder;

    @Override
    public List<ActivitiEventType> interest() {
        return Collections.singletonList(ActivitiEventType.PROCESS_COMPLETED);
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEntityEventImpl eventImpl = (ActivitiEntityEventImpl) event;
        ExecutionEntity entity = (ExecutionEntity) eventImpl.getEntity();

        // 封装流程上下文
        ProcessInstanceContext processInstanceContext = processInstanceContextBuilder.build(entity.getProcessInstanceId());
        // 根据流程结果进行不同的后处理

        ProcessResultHandleDispatcher.getInstance().fireResult(processInstanceContext);

        // 发送事件
        applicationEventPublisher.publishEvent(
                new ProcessCompleteEvent(this, processInstanceContext)
                                              );
    }

}
