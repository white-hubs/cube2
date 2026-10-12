package com.guhecloud.cube.base.flow.flowable.task.application.operation.transfer;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.flowable.common.message.MessageSender;
import com.guhecloud.cube.base.flow.flowable.common.message.NotifyUser;
import com.guhecloud.cube.base.flow.flowable.common.util.ProcessEngineAbility;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.AddMultiInstanceCmd;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.DeleteMultiInstanceCmd;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.AbstractTaskOperation;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.TaskOperatePostProcessor;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskLifeCycle;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskDelMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultTransfer extends AbstractTaskOperation<TransferCommand> implements Transfer {

    private static final String OR_SIGN_COMPLETION_CONDITION = "${nrOfCompletedInstances/nrOfInstances > 0}";
    private static final String NUMBER_OF_INSTANCES = "nrOfInstances";

    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final MessageSender messageSender;
    private final TaskOperatePostProcessor taskOperatePostProcessor;
    private final ManagementService managementService;

    @Override
    public void execute(TransferCommand transferCommand) {
        List<String> transferUserIdList = parseTargetAccounts(transferCommand.getTargetAccount());
        String first = transferUserIdList.get(0);
        TaskInfo currentTask = transferCommand.getTaskInfo();
        String sourceAccount = currentTask.getAssignee();

        boolean transferNode = ProcessEngineAbility.TRANSFER_NODE.enabled();
        List<Task> nodeTasks = transferNode ? getTransferNodeTasks(currentTask) : Collections.emptyList();
        List<String> removedTaskIds = nodeTasks.stream()
                .filter(task -> !task.getId().equals(currentTask.getId()))
                .map(Task::getId)
                .collect(Collectors.toList());
        Set<Long> workSourceUserIds = getWorkSourceUserIds(transferNode, nodeTasks, sourceAccount, transferUserIdList);

        if (!removedTaskIds.isEmpty()) {
            managementService.executeCommand(new DeleteMultiInstanceCmd(removedTaskIds));
        }
        taskService.setAssignee(currentTask.getId(), first);
        if (transferUserIdList.size() > 1) {
            for (String account : transferUserIdList.subList(1, transferUserIdList.size())) {
                managementService.executeCommand(new AddMultiInstanceCmd(currentTask.getId(), account));
            }
        }
        taskService.setVariable(currentTask.getId(), VariableConstant.APPROVAL_OPINION, transferCommand.getApprovalOpinion());
        taskOperatePostProcessor.after(currentTask, TaskOperationType.TRANSFER.name(), transferCommand);

        Optional<ExtProcessInstance> optional = extProcessInstanceRepository.get(currentTask.getProcessInstanceId());
        ExtProcessInstance extProcessInstance = optional.orElseThrow(() -> new ServiceException("找不到此流程"));
        // 发送消息通知
        messageSender.appendMessageBody("startUser", extProcessInstance.getStartUserInfo().getStartUserName())
                .appendMessageBody("formName", extProcessInstance.getProcessInstance().getProcessDefinitionName());
        send(extProcessInstance.getApplicationId(), extProcessInstance.getProcessInstance().getProcessInstanceId(), first);
        // 待办按任务处理，工作中心按流程实例和用户处理
        sendTodoMessage(currentTask, extProcessInstance, sourceAccount, first, removedTaskIds, workSourceUserIds);
    }

    private List<String> parseTargetAccounts(String targetAccount) {
        List<String> targetAccounts = Arrays.stream(targetAccount.split(","))
                .map(String::trim)
                .filter(account -> !account.isEmpty())
                .collect(Collectors.collectingAndThen(Collectors.toCollection(LinkedHashSet::new), List::copyOf));
        if (targetAccounts.isEmpty()) {
            throw new ServiceException("转交审批人不能为空");
        }
        try {
            targetAccounts.forEach(Long::parseLong);
        } catch (NumberFormatException e) {
            throw new ServiceException("转交审批人格式错误");
        }
        return targetAccounts;
    }

    private List<Task> getTransferNodeTasks(TaskInfo currentTask) {
        FlowElement flowElement = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId())
                .getFlowElement(currentTask.getTaskDefinitionKey());
        if (!(flowElement instanceof UserTask)) {
            throw new ServiceException("当前任务不是审批节点，不能执行节点转交");
        }

        MultiInstanceLoopCharacteristics loopCharacteristics = ((UserTask) flowElement).getLoopCharacteristics();
        if (loopCharacteristics == null || loopCharacteristics.isSequential()) {
            throw new ServiceException("节点转交仅支持或签或单人会签节点");
        }

        boolean orSign = OR_SIGN_COMPLETION_CONDITION.equals(loopCharacteristics.getCompletionCondition());
        if (!orSign) {
            Object nrOfInstances = taskService.getVariable(currentTask.getId(), NUMBER_OF_INSTANCES);
            if (!(nrOfInstances instanceof Number) || ((Number) nrOfInstances).intValue() != 1) {
                throw new ServiceException("会签节点只有一名审批人时才能执行节点转交");
            }
        }

        List<Task> nodeTasks = taskService.createTaskQuery()
                .processInstanceId(currentTask.getProcessInstanceId())
                .taskDefinitionKey(currentTask.getTaskDefinitionKey())
                .active()
                .list();
        boolean containsCurrentTask = nodeTasks.stream().anyMatch(task -> task.getId().equals(currentTask.getId()));
        if (!containsCurrentTask) {
            throw new ServiceException("当前任务不存在或已完成");
        }
        return nodeTasks;
    }

    private Set<Long> getWorkSourceUserIds(boolean transferNode, List<Task> nodeTasks, String sourceAccount,
                                           List<String> targetAccounts) {
        if (!transferNode) {
            return Collections.singleton(Long.parseLong(sourceAccount));
        }
        Set<Long> sourceUserIds = nodeTasks.stream()
                .map(Task::getAssignee)
                .filter(assignee -> assignee != null && !assignee.isBlank())
                .map(Long::parseLong)
                .collect(Collectors.toSet());
        Set<Long> targetUserIds = targetAccounts.stream().map(Long::parseLong).collect(Collectors.toSet());
        // 新审批人的任务创建消息已经写入工作中心，不能再将其人员状态更新为完成。
        sourceUserIds.removeAll(targetUserIds);
        return sourceUserIds;
    }

    private void send(ApplicationId applicationId, String processInstanceId, String targetAccount) {
        NotifyUser notifyUser = new NotifyUser(Long.parseLong(targetAccount));
        messageSender.appendApplication(applicationId)
                .appendProcessInstanceId(processInstanceId)
                .send("TASK_TRANSFER", Collections.singletonList(notifyUser));
    }

    private void sendTodoMessage(TaskInfo taskInfo, ExtProcessInstance extProcessInstance, String sourceAccount,
                                 String targetAccount, List<String> removedTaskIds, Set<Long> workSourceUserIds) {
        removedTaskIds.forEach(taskId -> RocketMQContextHolder.syncSendOrderly(
                TodoTaskFlowMessage.TOPIC,
                buildTodoDelMessage(taskId),
                extProcessInstance.getProcessInstance().getId()));

        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC,
                                              buildTodoMessage(extProcessInstance, taskInfo, sourceAccount, targetAccount),
                                              extProcessInstance.getProcessInstance().getId());

        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC,
                                              buildWorkMessage(extProcessInstance, taskInfo, targetAccount, workSourceUserIds),
                                              extProcessInstance.getProcessInstance().getId());
    }

    private TodoTaskDelMessage buildTodoDelMessage(String taskId) {
        TodoTaskDelMessage message = new TodoTaskDelMessage();
        message.setBusinessKey(taskId);
        message.setOperationType(TodoTaskFlowOperationEnum.DELETE);
        return message;
    }

    private TodoTaskFlowCirculationMessage buildTodoMessage(ExtProcessInstance extProcessInstance, TaskInfo task,
                                                            String sourceAccount, String targetAccount) {
        LocalDateTime date = LocalDateTime.now();
        TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
        message.setBusinessKey(task.getId());
        message.setBusinessStatus(TaskLifeCycle.ACTIVED.name());
        message.setUserIds(Collections.singleton(Long.parseLong(sourceAccount)));
        message.setHandlerTime(date);
        message.setGeneratedNextStatusTask(true);
        message.setTaskTime(DateUtils.convert(extProcessInstance.getProcessInstance().getStartTime()));
        message.setBusinessPrincipal(JsonUtils.serialize(extProcessInstance.getStartUserInfo()));
        message.setNextUserIds(Collections.singletonList(Long.parseLong(targetAccount)));
        message.setNextBusinessStatus(TaskLifeCycle.ACTIVED.name());
        message.setNextBusinessStatusName(TaskLifeCycle.ACTIVED.getLabel());
        message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
        return message;
    }

    private WorkTaskFlowCirculationMessage buildWorkMessage(ExtProcessInstance extProcessInstance, TaskInfo task,
                                                            String targetAccount, Set<Long> sourceUserIds) {
        LocalDateTime date = LocalDateTime.now();
        WorkTaskFlowCirculationMessage message = new WorkTaskFlowCirculationMessage();
        message.setBusinessKey(task.getProcessInstanceId());
        message.setBusinessStatus(TaskLifeCycle.ACTIVED.name());
        message.setUserIds(sourceUserIds);
        message.setHandlerTime(date);
        message.setGeneratedNextStatusTask(true);
        message.setTaskTime(DateUtils.convert(extProcessInstance.getProcessInstance().getStartTime()));
        message.setBusinessPrincipal(JsonUtils.serialize(extProcessInstance.getStartUserInfo()));
        message.setNextUserIds(Collections.singletonList(Long.parseLong(targetAccount)));
        message.setNextBusinessStatus(TaskLifeCycle.ACTIVED.name());
        message.setNextBusinessStatusName(TaskLifeCycle.ACTIVED.getLabel());
        message.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
        return message;
    }
}
