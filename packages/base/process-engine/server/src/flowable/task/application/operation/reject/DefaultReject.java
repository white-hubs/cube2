package com.guhecloud.cube.base.flow.flowable.task.application.operation.reject;

import base.constant.VariableConstant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.context.enumeration.RejectStrategyEnum;
import com.guhecloud.cube.base.flow.flowable.common.ProcessHelper;
import com.guhecloud.cube.base.flow.flowable.common.ProcessUserDTO;
import com.guhecloud.cube.base.flow.flowable.common.message.MessageSender;
import com.guhecloud.cube.base.flow.flowable.common.message.NotifyUser;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.application.querygateway.ProcessDefinitionQueryService;
import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.ProcessInstanceCommandService;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.Properties;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.CancellationCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.JumpCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.AbstractTaskOperation;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.TaskOperatePostProcessor;
import com.guhecloud.cube.base.flow.flowable.task.application.querygateway.TaskNodeQueryService;
import com.guhecloud.cube.base.flow.flowable.task.application.validator.TaskHandleValidator;
import com.guhecloud.cube.base.flow.flowable.task.domain.TaskInstance;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskLifeCycle;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.domain.repository.TaskInstanceRepository;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.guhecloud.cube.base.flow.flowable.common.ProcessService.TASK_RESULT_REJECTED;
import static com.guhecloud.cube.base.flow.flowable.common.ProcessService.TASK_RESULT_RETURNED;

@Service
@Scope("prototype")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultReject extends AbstractTaskOperation<RejectCommand> implements Reject {
    private final TaskHandleValidator taskHandleValidator;
    private final TaskInstanceRepository taskInstanceRepository;
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final ProcessHelper processHelper;
    private final TaskOperatePostProcessor taskOperatePostProcessor;
    private final MessageSender messageSender;
    private final ActivatedTaskCommandService activatedTaskCommandService;
    private final RepositoryService repositoryService;
    private final ProcessInstanceCommandService processInstanceCommandService;
    private final TaskNodeQueryService taskNodeQueryService;
    private final ManagementService managementService;
    private final ProcessDefinitionQueryService processDefinitionQueryService;
    private final HistoryService historyService;

    @Override
    public void execute(RejectCommand rejectCommand) {
        // 作废
        Task task = taskHandleValidator.attributionVerify(rejectCommand.getTaskId(), rejectCommand.getAccount());
        HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();
        ProcessDefinitionDTO processDefinitionDTO = processDefinitionQueryService.get(instance.getProcessDefinitionKey());
        ChildNode childNode = ExtProcessDefinition.getChildNode(JsonUtils.deserialize(processDefinitionDTO.getProcessFormJson(), new TypeReference<ChildNode>() {}),
                                                                task.getTaskDefinitionKey());
        Optional<TaskInstance> optional = taskInstanceRepository.get(rejectCommand.getTaskId());
        optional.ifPresent(taskInstance -> {
            final String processInstanceId = taskInstance.getTaskInfo().getProcessInstanceId();
            taskService.setVariable(task.getId(), VariableConstant.DATA, rejectCommand.getData());
            runtimeService.setVariable(task.getProcessInstanceId(), VariableConstant.RESULT, TaskOperationType.REJECT.name());
            if (taskInstance.isActived()) {
                // 设置任务结果
                taskInstance.setResult(TaskOperationType.REJECT.name());
                taskOperatePostProcessor.after(task, TaskOperationType.REJECT.name(), rejectCommand);
                //驳回操作
                this.doReject(task, rejectCommand, childNode.getProps(), instance.getStartUserId());
                String assignee = task.getAssignee();
                UserDTO user = ApplicationContextHolder.getBean(UserClient.class).getUser(Long.parseLong(assignee));
                rejectMsgSend(processInstanceId, user);
            }
        });
    }

    /**
     * 进行驳回操作
     *
     * @param task
     * @param rejectCommand
     * @param props
     * @param startUserId
     */
    private void doReject(Task task, RejectCommand rejectCommand, Properties props, String startUserId) {
        String variableKey = task.getTaskDefinitionKey() + VariableConstant.ASSIGNEE_List;
        //驳回到表单选择节点
        if (RejectStrategyEnum.TO_SELECT_NODE.equals(props.getRejectStrategy())) {
            //清楚当前节点审批人信息
            taskService.removeVariable(task.getId(), variableKey);
            List<Map<String, String>> rejectStrategyNode = props.getRejectStrategyNode();
            Assert.isNotAcceptable(CollectionUtils.isEmpty(rejectStrategyNode)
                                   || StringUtils.isBlank(rejectStrategyNode.get(0).get("id")), "未配置驳回节点");
            String nodeKey = rejectStrategyNode.get(0).get("id");
            String userTaskAssignee = "root".equals(nodeKey) ? startUserId : null;
            managementService.executeCommand(new JumpCommand(task.getId(), nodeKey, TASK_RESULT_REJECTED, userTaskAssignee));
            doneTodoMessage(task.getId(), task.getAssignee(), task.getProcessInstanceId());
            return;
        }
        //驳回到上一步
        if (RejectStrategyEnum.TO_PREV_NODE.equals(props.getRejectStrategy())) {
            // 获取最新完成的任务
            taskService.removeVariable(task.getId(), variableKey);
            String preNodeTaskDefinitionKey = taskNodeQueryService.getPreNodeTaskDefinitionKey(task);
            managementService.executeCommand(new JumpCommand(task.getId(), preNodeTaskDefinitionKey, TASK_RESULT_RETURNED, null));
            doneTodoMessage(task.getId(), task.getAssignee(), task.getProcessInstanceId());
            return;
        }

        if (RejectStrategyEnum.TO_INITIATOR.equals(props.getRejectStrategy())) {
            taskService.removeVariable(task.getId(), variableKey);
            //驳回到发起人
            managementService.executeCommand(new JumpCommand(task.getId(), VariableConstant.ROOT, TASK_RESULT_RETURNED, startUserId));
            doneTodoMessage(task.getId(), task.getAssignee(), task.getProcessInstanceId());
            return;
        }
        //终止流程
        CancellationCommand cancellationCommand = new CancellationCommand(
                task.getProcessInstanceId(),
                rejectCommand.getAccount(),
                rejectCommand.getComment()
        );
        activatedTaskCommandService.cancellation(cancellationCommand);
        processInstanceCommandService.endProcessInstance(TaskOperationType.REJECT, task.getProcessInstanceId(), TaskOperationType.REJECT.name());
    }

    /**
     * 消息通知
     *
     * @param processInstanceId 流程实例id
     */
    public void rejectMsgSend(String processInstanceId, UserDTO user) {
        final Optional<ExtProcessInstance> extProcessInstance = extProcessInstanceRepository.get(processInstanceId);
        extProcessInstance.ifPresent(instance -> {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(instance.getProcessDefinitionKey()).singleResult();
            ProcessUserDTO processStartUser = processHelper.getProcessStartUser(instance.getProcessInstance().getProcessInstanceId());
            // 异步执行
            messageSender.appendMessageBody("formName", processDefinition.getName())
                    .appendMessageBody("approvalUser", ObjectUtils.isNotEmpty(user) ? user.getName() : "")
                    .appendApplication(instance.getApplicationId())
                    .appendProcessInstanceId(instance.getProcessInstance().getProcessInstanceId())
                    .send("TASK_REFUSE", Collections.singletonList(new NotifyUser(Long.parseLong(processStartUser.getId()))));
        });
    }

    /**
     * 处理代办
     *
     * @param businessKey
     * @param assignee
     *
     * @return
     */
    private void doneTodoMessage(String businessKey, String assignee, String processInstanceId) {
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
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC, message, processInstanceId);
    }
}
