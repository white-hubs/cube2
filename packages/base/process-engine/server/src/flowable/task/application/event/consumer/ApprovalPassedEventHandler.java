package com.guhecloud.cube.base.flow.flowable.task.application.event.consumer;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.common.event.EventListener;
import com.guhecloud.cube.base.flow.flowable.common.message.MessageSender;
import com.guhecloud.cube.base.flow.flowable.common.message.NotifyUser;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessExecuteContext;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessInfo;
import com.guhecloud.cube.base.flow.flowable.task.domain.TaskInstanceDomainService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.event.ActivitiActivityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/9/27 15:28
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApprovalPassedEventHandler extends EventListener implements TaskInstanceEventHandler {
    private final MessageSender messageSender;

    @Override
    public void create(DelegateTask delegateTask) {
        boolean firstTask = TaskInstanceDomainService.isFirstTask((ExecutionEntity) delegateTask.getExecution());
        if (!firstTask) {
            Object formInstanceId = delegateTask.getVariable(VariableConstant.FORM_INSTANCE_Id);
            if (StringUtils.isNotEmpty(formInstanceId.toString())) {
                ProcessInfo processInfo = ProcessExecuteContext.getProcessInfo(delegateTask.getProcessInstanceId());
                messageSender
                        .appendMessageBody("formName", processInfo.getInstanceName())
                        .appendApplication(processInfo.getApplicationId())
                        .appendProcessInstanceId(processInfo.getId())
                        .appendMessageBody("startUser", processInfo.getStartUserInfo().getStartUserName())
                        .send("TASK_BACKLOG", Collections.singletonList(new NotifyUser(Long.parseLong(delegateTask.getAssignee()))));
            }
        }
    }

    @Override
    public void assignment(DelegateTask delegateTask) {

    }

    @Override
    public void complete(DelegateTask delegateTask) {
        // 推送消息给发起人
        boolean firstTask = TaskInstanceDomainService.isFirstTask((ExecutionEntity) delegateTask.getExecution());
        if (!firstTask) {
            delegateTask.getVariable(VariableConstant.RESULT);
        }

    }

    @Override
    public List<ActivitiEventType> interest() {
        return Collections.singletonList(ActivitiEventType.ACTIVITY_COMPLETED);
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        //如果是任务节点，并且不是第一个才执行
        ActivitiActivityEvent activitiActivityEvent = (ActivitiActivityEvent) event;
        String activityType = activitiActivityEvent.getActivityType();
        if (activityType.equals("userTask")) {

        }
    }
}
