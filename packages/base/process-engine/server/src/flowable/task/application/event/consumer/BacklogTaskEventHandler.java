package com.guhecloud.cube.base.flow.flowable.task.application.event.consumer;

import base.constant.VariableConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowBusinessQueryService;
import com.guhecloud.cube.base.flow.context.querygateway.OverdueAutomaticApprovalService;
import com.guhecloud.cube.base.flow.flowable.common.resovler.AutoPassMessageTopic;
import com.guhecloud.cube.base.flow.flowable.data.AutoPassObject;
import com.guhecloud.cube.base.flow.flowable.process.application.enumeration.FlowResult;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.ProcessInstanceContext;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.cube.base.flow.flowable.resolver.enumeration.AssigneeTypeEnums;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessExecuteContext;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessInfo;
import com.guhecloud.cube.base.flow.flowable.scene.approval.domain.enumeration.ApprovalResult;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.TaskOperatePostProcessor;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.application.querygateway.HistoricalTaskQueryService;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskLifeCycle;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.domain.valueobject.AutoPassTodoMessage;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import instance.command.CompleteTaskCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/9/5 15:25
 * @description
 */
@Order(1)
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogTaskEventHandler implements TaskInstanceEventHandler {
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final TaskService taskService;
    private final HistoricalTaskQueryService historicalTaskQueryService;
    private final HistoryService historyService;
    private final ProcessInstanceContext processInstanceContext;
    private final RuntimeService runtimeService;
    private final TaskOperatePostProcessor taskOperatePostProcessor;
    private final FormFlowBusinessQueryService formFlowBusinessQueryService;
    private final UserClient userClient;
    private final RedisTemplate redisTemplate;
    private final OverdueAutomaticApprovalService overdueAutomaticApprovalService;
    private final ExtProcessInstanceMapper processInstanceMapper;

    @Override
    public void create(DelegateTask delegateTask) {
        AutoPassObject autoPass = isAutoPass(delegateTask);
        if ("100000".equals(delegateTask.getAssignee()) || autoPass.isAutoPass()) {
            //自动审批通过
            autoCompleteTask(delegateTask, autoPass.getApprovalOpinion(), autoPass.getAssigneeId());
        }
    }

    /**
     * @Author wql
     * @Description 判断任务是否需要自动通过审批
     * @Date 11:26 2023/5/22
     **/
    private AutoPassObject isAutoPass(DelegateTask delegateTask) {
        //获取当前流程回退后的第一个节点
        String nodeKey = (String) redisTemplate.opsForHash().get(VariableConstant.BACK_NODE, delegateTask.getProcessInstanceId());
        if (StringUtils.isBlank(nodeKey) || !delegateTask.getTaskDefinitionKey().equals(nodeKey)) {
            ProcessInfo processInfo = ProcessExecuteContext.getProcessInfo(delegateTask.getProcessInstanceId());
            String assignedType = ProcessExecuteContext.getProcessNodeProperties(delegateTask.getProcessInstanceId(),
                                                                                 delegateTask.getTaskDefinitionKey()).getAssignedType();
            if (!Boolean.TRUE.equals(processInfo.getDeDuplication()) ||
                AssigneeTypeEnums.PREV_ASSIGNEE.name().equals(assignedType)) {
                return AutoPassObject.builder()
                        .isAutoPass(false)
                        .approvalOpinion(ApprovalOpinionSupport.EMPTY.getApprovalOpinion())
                        .build();
            }
            //2.拿到历史审批人员
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(delegateTask.getProcessInstanceId())
                    .includeTaskLocalVariables()
                    .orderByTaskCreateTime().asc().list();
            //3.过滤会签节点自动审批的任务
            List<HistoricTaskInstance> collect = list.stream().filter(s -> ObjectUtils.isEmpty(s.getDeleteReason()))
                    .toList();
            //判断当前任务审批人中是否包含历史审批人员
            if (collect.size() > 1) {
                String assignee = delegateTask.getAssignee();
                Set<String> assignees =
                        StringUtils.isBlank(assignee) ? new HashSet<>() : Arrays.stream(assignee.split(",")).collect(Collectors.toSet());
                for (int i = 1; i < collect.size(); i++) {
                    HistoricTaskInstance instance = collect.get(i);
                    if (assignees.contains(instance.getAssignee())
                        && !StringUtils.equals(instance.getTaskDefinitionKey(), delegateTask.getTaskDefinitionKey())) {
                        String oldAssignee = instance.getAssignee();
                        //审批意见
                        Object opinion = instance.getTaskLocalVariables().get(VariableConstant.APPROVAL_OPINION);
                        //节点审批结果
                        String result = (String) instance.getTaskLocalVariables().get(VariableConstant.TASK_RESULT);
                        if (ObjectUtils.isNotEmpty(result) && FlowResult.PASS.name().equals(result) &&
                            ObjectUtils.isNotEmpty(opinion)) {
                            Boolean autoPass = this.necessaryForm(processInfo.getChildNode(), processInfo.getFormJson(), delegateTask.getTaskDefinitionKey());
                            return AutoPassObject.builder()
                                    .isAutoPass(autoPass)
                                    .approvalOpinion((ApprovalOpinion) opinion)
                                    .assigneeId(Long.parseLong(oldAssignee))
                                    .build();
                        }
                    }
                }
            }
        }
        return AutoPassObject.builder()
                .isAutoPass(false)
                .approvalOpinion(ApprovalOpinionSupport.EMPTY.getApprovalOpinion())
                .build();
    }

    /**
     * @Author wql
     * @Description 自动通过当前任务
     * @Date 11:26 2023/5/22
     **/
    private void autoCompleteTask(DelegateTask delegateTask, ApprovalOpinion value, Long assigneeId) {
        CompleteTaskCommand completeTaskCommand =
                CompleteTaskCommand.builder()
                        .taskId(delegateTask.getId())
                        .result(TaskOperationType.PASS.name())
                        .account(delegateTask.getAssignee())
                        .approvalOpinion(value)
                        .build();
        Task task = (Task) delegateTask;
        // 获取扩展流程实例
        Optional<ExtProcessInstance> optional = extProcessInstanceRepository.get(task.getProcessInstanceId());
        optional.ifPresent(extProcessInstance -> {
            extProcessInstance.getDataMap().put(task.getTaskDefinitionKey(), completeTaskCommand.getData());
            extProcessInstanceRepository.save(extProcessInstance);
        });
        String assigneeName = "";
        if (assigneeId != null) {
            UserDTO user = userClient.getUser(assigneeId);
            assigneeName = Objects.nonNull(user) ? "_" + user.getName() : "";
        }
        List<TaskLog.Operator> operators = Collections.singletonList(new TaskLog.Operator(Long.parseLong(delegateTask.getAssignee()),
                                                                                          "自动通过" + assigneeName));
        taskOperatePostProcessor.after(task, TaskOperationType.PASS.name(), operators, completeTaskCommand);
        taskService.setVariablesLocal(task.getId(), completeTaskCommand.getVariables());
        try {
            taskService.complete(delegateTask.getId());
            checkAndSetProcessInstanceState(delegateTask.getProcessInstanceId());
        } catch (ActivitiException e) {
            if (e.getMessage().contains("No outgoing sequence flow of the exclusive gateway")) {
                throw new ServiceException("请先配置流程分支");
            }
        }
        sendHaveDoneMessage(task);
        sendOtherNodeTaskDeleteMessage(delegateTask);
    }

    private void checkAndSetProcessInstanceState(String processInstanceId) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        if (ObjectUtils.isNotEmpty(list)) {
            runtimeService.removeVariable(processInstanceId, VariableConstant.RESULT);
        }
    }

    /**
     * @Author wql
     * @Description 发送或签节点待办删除消息
     * @Date 11:00 2023/7/2
     **/
    private void sendOtherNodeTaskDeleteMessage(DelegateTask delegateTask) {
        FlowElement currentFlowElement = delegateTask.getExecution().getCurrentFlowElement();
        if (currentFlowElement instanceof UserTask) {
            UserTask userTask = (UserTask) currentFlowElement;
            String completionCondition = userTask.getLoopCharacteristics().getCompletionCondition();
            //只有或签节点自动通过需要处理
            if (StringUtils.isNotBlank(completionCondition) && "${nrOfCompletedInstances/nrOfInstances > 0}".equals(completionCondition)) {
                AutoPassTodoMessage build = AutoPassTodoMessage.builder()
                        .id(delegateTask.getId())
                        .taskDefinitionKey(delegateTask.getTaskDefinitionKey())
                        .processInstanceId(delegateTask.getProcessInstanceId())
                        .build();
                RocketMQContextHolder.asyncSend(AutoPassMessageTopic.TOPIC, build, 3);
            }
        }
    }

    private void sendHaveDoneMessage(Task task) {
        RocketMQContextHolder.syncSendOrderly(
                TodoTaskFlowMessage.TOPIC,
                buildTodoMessage(task.getId(),
                                 task.getAssignee()),
                task.getProcessInstanceId());

        RocketMQContextHolder.syncSendOrderly(
                WorkTaskFlowCirculationMessage.TOPIC,
                buildWorkMessage(task.getProcessInstanceId(),
                                 task.getAssignee()),
                task.getProcessInstanceId());
    }

    private TodoTaskFlowCirculationMessage buildTodoMessage(String businessKey, String assignee) {
        TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
        message.setBusinessKey(businessKey);
        message.setBusinessStatus(TaskLifeCycle.ACTIVED.name());
        message.setUserIds(Collections.singleton(Long.parseLong(assignee)));
        message.setHandlerTime(LocalDateTime.now());
        message.setGeneratedNextStatusTask(false);
        message.setNextBusinessStatus(TaskLifeCycle.COMPLEATED.name());
        message.setNextBusinessStatusName(TaskLifeCycle.COMPLEATED.getLabel());
        message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
        return message;
    }

    private WorkTaskFlowCirculationMessage buildWorkMessage(String businessKey, String assignee) {
        WorkTaskFlowCirculationMessage message = new WorkTaskFlowCirculationMessage();
        message.setBusinessKey(businessKey);
        message.setBusinessStatus(TaskLifeCycle.ACTIVED.name());
        message.setUserIds(Collections.singleton(Long.parseLong(assignee)));
        message.setHandlerTime(LocalDateTime.now());
        message.setGeneratedNextStatusTask(false);
        message.setNextBusinessStatus(TaskLifeCycle.ACTIVED.name());
        message.setNextBusinessStatusName(TaskLifeCycle.ACTIVED.getLabel());
        message.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
        return message;
    }

    @Override
    public void assignment(DelegateTask delegateTask) {

    }

    @Override
    public void complete(DelegateTask delegateTask) {
        processInstanceContext.setLastFinishedTask((TaskEntity) delegateTask);
        // 审批通过后
        Object result = delegateTask.getVariable(VariableConstant.RESULT);
        if (result == ApprovalResult.PASS) {

        }

    }

    /**
     * 判断该审批节点是否有必填表单
     *
     * @return 是否有必填项未完成
     *
     * @Param flowJson 流程json
     * @Param formJson 表单json
     * @Param nodeId   节点id
     */
    private Boolean necessaryForm(ChildNode childNode, String formJson, String nodeId) {
        log.info("necessaryForm入参:流程json:{}，表单json：{}，节点id{}", childNode, formJson, nodeId);
        if (ObjectUtils.isNotEmpty(childNode) && ObjectUtils.isNotEmpty(childNode.getProps()) &&
            ObjectUtils.isNotEmpty(childNode.getProps().getFormPerms())) {
            // 判断该节点是否有必填表单
            for (var formPerms : childNode.getProps().getFormPerms()) {
                // 是否必填
                if (Objects.nonNull(formPerms.getRequired()) && formPerms.getRequired()) {
                    // 判断当前任务是否包含该表单，E=可编辑
                    if ("E".equals(formPerms.getPerm())) {
                        // 将表单转为json数组
                        JsonNode jsonArray = JsonUtils.deserialize(formJson);
                        for (JsonNode ex : jsonArray) {
                            String id = JsonUtils.getNodeValue(ex, "id");
                            if (StringUtils.equals(id, formPerms.getId())) {
                                // 获取表单数据
                                JsonNode value = ex.get("value");
                                return ObjectUtils.isNotEmpty(value);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
