package com.guhecloud.cube.base.flow.flowable.task.application.event.consumer;

import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessExecuteContext;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessInfo;
import com.guhecloud.cube.base.flow.flowable.task.domain.TaskInstanceDomainService;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskLifeCycle;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskDelMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/11/1 9:51
 * @description 待办消息发送
 */
@Order(0)
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoMessageTaskEventListener implements TaskInstanceEventHandler {

    private final ExtProcessInstanceRepository extProcessInstanceRepository;

    private final TaskService taskService;

    @Override
    public void create(DelegateTask delegateTask) {

        //  如果是第一个任务不用管
        boolean firstTask = TaskInstanceDomainService.isFirstTask((ExecutionEntity) delegateTask.getExecution());
        if (!firstTask) {
            ProcessInfo processInfo = ProcessExecuteContext.getProcessInfo(delegateTask.getProcessInstanceId());
            //创建消息体
            TodoTaskInitMessage todoTaskMessage = TodoTaskInitMessage.builder()
                    .applicationCode(processInfo.getApplicationId().getApplicationCode())
                    .applicationName(processInfo.getApplicationId().getApplicationName())
                    .businessKey(delegateTask.getId())
                    .businessType(processInfo.getBusinessId().getBusinessType())
                    .businessTypeName(processInfo.getBusinessId().getBusinessTypeName())
                    .taskName(StringUtils.isNotBlank(processInfo.getBacklogTitle()) ? processInfo.getBacklogTitle() : delegateTask.getName())
                    .taskTime(processInfo.getStartTime())
                    .userIds(Collections.singletonList(Long.parseLong(delegateTask.getAssignee())))
                    .businessStatus(TaskLifeCycle.ACTIVED.name())
                    .businessPrincipal(JsonUtils.serialize(processInfo.getStartUserInfo()))
                    .businessStatusName(TaskLifeCycle.ACTIVED.getLabel())
                    .operationType(TodoTaskFlowOperationEnum.INSERT)
                    .build();

            //发消息
            RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC, todoTaskMessage, processInfo.getId());
            sendWorkCenterMessage(processInfo, delegateTask);
        }
    }

    private void sendWorkCenterMessage(ProcessInfo extProcessInstance, DelegateTask delegateTask) {
        //创建消息体
        WorkTaskInitMessage workTaskMessage = WorkTaskInitMessage.builder()
                .applicationCode(extProcessInstance.getApplicationId().getApplicationCode())
                .applicationName(extProcessInstance.getApplicationId().getApplicationName())
                .businessKey(delegateTask.getProcessInstanceId())
                .businessType(extProcessInstance.getBusinessId().getBusinessType())
                .businessTypeName(extProcessInstance.getBusinessId().getBusinessTypeName())
                .taskName(StringUtils.isNotBlank(extProcessInstance.getBacklogTitle()) ? extProcessInstance.getBacklogTitle() : delegateTask.getName())
                .taskTime(extProcessInstance.getStartTime())
                .userIds(Collections.singletonList(Long.parseLong(delegateTask.getAssignee())))
                .businessStatus(TaskLifeCycle.ACTIVED.name())
                .businessPrincipal(JsonUtils.serialize(extProcessInstance.getStartUserInfo()))
                .businessStatusName(TaskLifeCycle.ACTIVED.getLabel())
                .operationType(WorkTaskFlowOperationEnum.INSERT)
                .build();

        //发消息
        RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, workTaskMessage, extProcessInstance
                .getId());
    }

    @Override
    public void assignment(DelegateTask delegateTask) {

    }

    @Override
    public void complete(DelegateTask delegateTask) {
        boolean firstTask = TaskInstanceDomainService.isFirstTask((ExecutionEntity) delegateTask.getExecution());
        if (!firstTask) {
            Optional<ExtProcessInstance> optional = extProcessInstanceRepository.get(delegateTask.getProcessInstanceId());
            optional.ifPresent(extProcessInstance -> {

                // 判断是否是节点通过 存在会签和或签
                FlowElement currentFlowElement = delegateTask.getExecution().getCurrentFlowElement();
                if (currentFlowElement instanceof UserTask) {
                    UserTask userTask = (UserTask) currentFlowElement;
                    String completionCondition = userTask.getLoopCharacteristics().getCompletionCondition();
                    if (StringUtils.isNotBlank(completionCondition) &&
                        completionCondition.equals("${nrOfCompletedInstances/nrOfInstances > 0}")) {
                        List<Task> taskList = taskService.createTaskQuery().taskDefinitionKey(delegateTask.getTaskDefinitionKey())
                                .processInstanceId(delegateTask.getProcessInstanceId())
                                .list().stream().filter(task -> !task.getId().equals(delegateTask.getId())).collect(Collectors.toList());
                        taskList.forEach(task -> {
                            TodoTaskDelMessage todoTaskDelMessage = new TodoTaskDelMessage();
                            todoTaskDelMessage.setBusinessKey(task.getId());
                            todoTaskDelMessage.setOperationType(TodoTaskFlowOperationEnum.DELETE);
                            RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC, todoTaskDelMessage, delegateTask.getProcessInstanceId());
                        });
                    }
                }
                LocalDateTime now = LocalDateTime.now();
                TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
                message.setBusinessKey(delegateTask.getId());
                message.setBusinessStatus(TaskLifeCycle.ACTIVED.name());
                message.setUserId(Long.parseLong(delegateTask.getAssignee()));
                message.setHandlerTime(now);
                message.setTaskTime(now);
                message.setBusinessPrincipal(JsonUtils.serialize(extProcessInstance.getStartUserInfo()));
                message.setGeneratedNextStatusTask(false);
                message.setNextBusinessStatus(TaskLifeCycle.COMPLEATED.name());
                message.setNextBusinessStatusName(TaskLifeCycle.COMPLEATED.getLabel());
                message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
                //发消息
                RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC, message, delegateTask.getProcessInstanceId());

                WorkTaskFlowCirculationMessage workMessage = new WorkTaskFlowCirculationMessage();
                workMessage.setBusinessKey(delegateTask.getProcessInstanceId());
                workMessage.setBusinessStatus(TaskLifeCycle.ACTIVED.name());
                workMessage.setUserId(Long.parseLong(delegateTask.getAssignee()));
                workMessage.setHandlerTime(now);
                workMessage.setTaskTime(now);
                workMessage.setBusinessPrincipal(JsonUtils.serialize(extProcessInstance.getStartUserInfo()));
                workMessage.setGeneratedNextStatusTask(false);
                workMessage.setNextBusinessStatus(TaskLifeCycle.ACTIVED.name());
                workMessage.setNextBusinessStatusName(TaskLifeCycle.ACTIVED.getLabel());
                workMessage.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
                RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, workMessage, delegateTask.getProcessInstanceId());
            });

        }

    }
}
