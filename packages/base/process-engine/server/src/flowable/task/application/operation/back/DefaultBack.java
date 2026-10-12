package com.guhecloud.cube.base.flow.flowable.task.application.operation.back;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.flowable.common.ProcessHelper;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.JumpCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.AbstractTaskOperation;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.TaskOperatePostProcessor;
import com.guhecloud.cube.base.flow.flowable.task.application.querygateway.TaskNodeQueryService;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskLifeCycle;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskDelMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ManagementService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.guhecloud.cube.base.flow.flowable.common.ProcessService.TASK_RESULT_RETURNED;

@Slf4j
@Scope("prototype")
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultBack extends AbstractTaskOperation<BackCommand> implements Back {
    private final TaskService taskService;
    private final ProcessHelper helper;
    private final ManagementService managementService;
    private final TaskNodeQueryService taskNodeQueryService;
    private final TaskOperatePostProcessor taskOperatePostProcessor;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void execute(BackCommand backCommand) {
        String operation =
                ObjectUtils.isNotEmpty(backCommand.getOperation()) ? backCommand.getOperation().name() : TaskOperationType.BACK.name();
        // 变量清理
        // 任务结果设置
        helper.setResultTaskVariablesLocal(backCommand.getTaskInfo().getId(), backCommand.getComment(), TASK_RESULT_RETURNED);
        taskService.setVariable(backCommand.getTaskInfo().getId(), VariableConstant.RESULT, operation);
        taskService.setVariable(backCommand.getTaskInfo().getId(), VariableConstant.APPROVAL_OPINION, backCommand.getApprovalOpinion());
        // 获取最新完成的任务
        String preNodeTaskDefinitionKey = taskNodeQueryService.getPreNodeTaskDefinitionKey(backCommand.getTaskInfo());

        redisTemplate.opsForHash()
                .put(VariableConstant.BACK_NODE, backCommand.getTaskInfo().getProcessInstanceId(), preNodeTaskDefinitionKey);

        // 删除待办-除了当前任务，删除正在进行中的任务的待办
        sendTodoTask(backCommand.getTaskInfo());

        taskService.setVariableLocal(backCommand.getTaskInfo().getId(), VariableConstant.TASK_RESULT, operation);
        String variableKey = backCommand.getTaskInfo().getTaskDefinitionKey() + VariableConstant.ASSIGNEE_List;
        taskService.removeVariable(backCommand.getTaskInfo().getId(), variableKey);
        managementService.executeCommand(new JumpCommand(backCommand.getTaskInfo()
                                                                 .getId(), preNodeTaskDefinitionKey, TASK_RESULT_RETURNED, null));

        taskOperatePostProcessor.after(backCommand.getTaskInfo(), operation, backCommand);

    }

    /**
     * 发送待办消息
     *
     * @param task 节点任务
     */
    public void sendTodoTask(TaskInfo task) {
        log.info("发送rocket消息processInstanceId:{},taskDefinitionKey:{}", task.getProcessInstanceId(), task.getTaskDefinitionKey());
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId())
                .taskDefinitionKey(task.getTaskDefinitionKey())
                .list();
        // 工作中心退回
        RocketMQContextHolder.syncSendOrderly(
                WorkTaskFlowCirculationMessage.TOPIC,
                buildWorkMessage(task),
                task.getProcessInstanceId());
        ExecutorContextHolder.run(() -> {
            tasks.stream().filter(taskinfo -> !taskinfo.getId().equals(task.getId()))
                    .forEach(deleteOfTask -> {
                        RocketMQContextHolder.syncSendOrderly(
                                TodoTaskFlowMessage.TOPIC,
                                buildTodoDelMessage(deleteOfTask.getId()),
                                deleteOfTask.getProcessInstanceId());
                    });
            tasks.stream().filter(taskinfo -> taskinfo.getId().equals(task.getId())).forEach(completeOfTask -> {
                //发消息
                log.info("发送rocket消息businessKey:{},assignee:{},tenantId:{}", completeOfTask.getId(), completeOfTask.getAssignee(), completeOfTask.getTenantId());
                SessionContextHolder.requestTenant(Integer.valueOf(completeOfTask.getTenantId()));
                RocketMQContextHolder.syncSendOrderly(
                        TodoTaskFlowMessage.TOPIC,
                        buildTodoMessage(completeOfTask.getId(),
                                         completeOfTask.getAssignee()),
                        task.getProcessInstanceId());
            });
        });
    }

    private TodoTaskDelMessage buildTodoDelMessage(String taskId) {
        TodoTaskDelMessage todoTaskDelMessage = new TodoTaskDelMessage();
        todoTaskDelMessage.setBusinessKey(taskId);
        todoTaskDelMessage.setOperationType(TodoTaskFlowOperationEnum.DELETE);
        return todoTaskDelMessage;
    }

    private TodoTaskFlowCirculationMessage buildTodoMessage(String businessKey, String assignee) {
        TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
        message.setBusinessKey(businessKey);
        message.setBusinessStatus(TaskLifeCycle.ACTIVED.name());
        message.setUserIds(Collections.singleton(Long.parseLong(assignee)));
        message.setUserId(Long.parseLong(assignee));
        message.setHandlerTime(LocalDateTime.now());
        message.setGeneratedNextStatusTask(false);
        message.setNextBusinessStatus(TaskLifeCycle.COMPLEATED.name());
        message.setNextBusinessStatusName(TaskLifeCycle.COMPLEATED.getLabel());
        message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
        return message;
    }

    private WorkTaskFlowCirculationMessage buildWorkMessage(TaskInfo task) {
        WorkTaskFlowCirculationMessage message = new WorkTaskFlowCirculationMessage();
        message.setBusinessKey(task.getProcessInstanceId());
        message.setBusinessStatus(TaskLifeCycle.ACTIVED.name());
        message.setUserIds(Collections.singleton(Long.parseLong(task.getAssignee())));
        message.setHandlerTime(LocalDateTime.now());
        message.setGeneratedNextStatusTask(false);
        message.setNextBusinessStatus(TaskLifeCycle.ACTIVED.name());
        message.setNextBusinessStatusName(TaskLifeCycle.ACTIVED.getLabel());
        message.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
        return message;
    }
}
