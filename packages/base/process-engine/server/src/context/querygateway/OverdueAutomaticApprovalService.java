package com.guhecloud.cube.base.flow.context.querygateway;

import base.constant.VariableConstant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.client.doamin.enumeration.ApprovalTypeEnum;
import com.guhecloud.cube.base.flow.client.dto.TaskVariableDTO;
import com.guhecloud.cube.base.flow.context.enumeration.NotifyTypeEnum;
import com.guhecloud.cube.base.flow.flowable.common.message.MessageSender;
import com.guhecloud.cube.base.flow.flowable.common.message.NotifyUser;
import com.guhecloud.cube.base.flow.flowable.common.resovler.AutoPassMessageTopic;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.application.querygateway.ProcessDefinitionQueryService;
import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.Properties;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.CommandExecutor;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.TaskOperatePostProcessor;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.FlowDateUnitEnum;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskLifeCycle;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.domain.valueobject.AutoPassTodoMessage;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskRuNodeMapper;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.LimitTimeApprovalConfigDTO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.LimitTimeApprovalDTO;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import instance.command.CompleteTaskCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2024-05-24  00:00
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OverdueAutomaticApprovalService {
    private final TaskRuNodeMapper taskRuNodeMapper;
    private final ExtProcessInstanceMapper extProcessInstanceMapper;
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final ProcessDefinitionQueryService processDefinitionQueryService;
    private final TaskService taskService;
    private final TaskOperatePostProcessor taskOperatePostProcessor;
    private final CommandExecutor commandExecutor;
    public static final ConcurrentHashMap<String, ProcessDefinitionDTO> PROCESS_FLOW_JSON_CACHE = new ConcurrentHashMap<>();

    public void checkOverdueTasks() {
        // 获取当前时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = formatter.format(LocalDateTime.now());
        var dateTime = DateUtils.parse(formattedDate + ":00", "yyyy-MM-dd HH:mm:ss");
        var endDate = DateUtils.parse(formattedDate + ":59", "yyyy-MM-dd HH:mm:ss");
        var startDate = DateUtils.offsetMinute(dateTime, -4).withNano(0);
        log.debug("开始时间：{}：结束时间：{}:开始检查需要自动审批通过流程", startDate, endDate);
        // 进行中的审批任务
        List<TaskVariableDTO> taskVariableDTOS = taskRuNodeMapper.queryTaskByVariables(endDate);
        log.debug("进行中的审批任务数量：{}", taskVariableDTOS.size());
        if (CollectionUtils.isEmpty(taskVariableDTOS)) {
            return;
        }
        List<String> processInstanceIds = taskVariableDTOS.stream().map(TaskVariableDTO::getProcessInstanceId).distinct()
                .collect(Collectors.toList());
        Map<String, ExtProcessInstance> extProcessInstanceMap = extProcessInstanceRepository.getBatch(processInstanceIds);
        taskVariableDTOS.forEach(taskVariableDTO -> {
            if (Objects.isNull(extProcessInstanceMap.get(taskVariableDTO.getProcessInstanceId()))) {
                return;
            }
            try {
                Principal principal = Principal.builder()
                        .id(taskVariableDTO.getAssignee())
                        .type(Principal.Type.USER)
                        .build();
                SecurityContextHolder.setPrincipal(principal);
                SessionContextHolder.requestTenant(taskVariableDTO.getTenantId());
                log.debug("租户编号：{}", SessionContextHolder.getRequester().getTerminal());
                ExtProcessInstance extProcessInstance = extProcessInstanceMap.get(taskVariableDTO.getProcessInstanceId());
                log.debug("流程：{}；任务：{}：检查需要自动审批通过流程", extProcessInstance.getInstanceName(), taskVariableDTO.getId());
                ProcessDefinitionDTO definitionDTO;
                if (PROCESS_FLOW_JSON_CACHE.containsKey(extProcessInstance.getProcessDefinitionKey())) {
                    definitionDTO = PROCESS_FLOW_JSON_CACHE.get(extProcessInstance.getProcessDefinitionKey());
                } else {
                    definitionDTO = processDefinitionQueryService.get(extProcessInstance.getProcessDefinitionKey());
                    PROCESS_FLOW_JSON_CACHE.put(extProcessInstance.getProcessDefinitionKey(), definitionDTO);
                }
                String json = definitionDTO.getProcessFormJson();
                ChildNode childNode = ExtProcessDefinition
                        .getChildNode(JsonUtils.deserialize(json, new TypeReference<ChildNode>() {}), taskVariableDTO.getNodeId());
                if (Objects.nonNull(childNode) && Objects.nonNull(childNode.getProps()) &&
                    Objects.nonNull(childNode.getProps().getLimitTimeApprovalConfig())) {
                    log.debug("流程json:{}", json);
                    this.overdueApprovalReminder(startDate, endDate, childNode.getProps(), taskVariableDTO, extProcessInstance);
                    this.overdueAutomaticApproval(definitionDTO.getProcess(), endDate, childNode.getProps(), taskVariableDTO, extProcessInstance);
                }
            } catch (Exception e) {
                log.error("{}任务{}：检查需要自动审批通过流程失败", taskVariableDTO.getProcessInstanceId(), taskVariableDTO.getId(), e);
            }
        });
        SessionContextHolder.clearSessionContext();
        PROCESS_FLOW_JSON_CACHE.clear();
        log.debug("检查需要自动审批通过流程结束");
    }

    /**
     * 审批预期提醒
     *
     * @param properties         消息配置参数
     * @param taskVariable       任务变量
     * @param extProcessInstance
     */
    private void overdueApprovalReminder(LocalDateTime startDate, LocalDateTime endDate, Properties properties,
                                         TaskVariableDTO taskVariable,
                                         ExtProcessInstance extProcessInstance
                                        ) {
        try {
            log.debug("开始提醒超时未审批的流程,{}", JsonUtils.serialize(properties));
            LimitTimeApprovalConfigDTO configDTO = properties.getLimitTimeApprovalConfig();
            if (ObjectUtils.isNotEmpty(configDTO.getLimitTimeNotify()) &&
                Boolean.TRUE.equals(configDTO.getLimitTimeNotify().getChecked()) &&
                CollectionUtils.isNotEmpty(configDTO.getLimitTimeNotify().getApprovalReminder())) {
                configDTO.getLimitTimeNotify().getApprovalReminder().forEach(reminder -> {
                    var calculatedTime = DateUtils.offsetMinute(taskVariable.getCreateTime(), this.getMinutes(reminder.getUnit(), reminder.getValue()));
                    if (!calculatedTime.isBefore(startDate) && !calculatedTime.isAfter(endDate)) {
                        List<NotifyUser> notifyUsers = NotifyTypeEnum.valueOf(reminder.getNotifyType())
                                .getNotifyTypeUser(taskVariable, reminder, extProcessInstance);
                        if (CollectionUtils.isNotEmpty(notifyUsers)) {
                            // 发送消息
                            ApplicationContextHolder.getBean(MessageSender.class)
                                    .appendMessageBody("formName", extProcessInstance.getExtProcessInstancePO().getInstanceName())
                                    .appendMessageBody("value",
                                                       reminder.getValue() +
                                                       FlowDateUnitEnum.getByName(reminder.getUnit()).getDescription())
                                    .appendApplication(extProcessInstance.getApplicationId())
                                    .appendProcessInstanceId(taskVariable.getId())
                                    .send("TASK_OVERDUE", notifyUsers);

                        }
                    }
                });
            }
            log.debug("提醒超时未审批的流程结束");
        } catch (Exception e) {
            log.error("提醒超时未审批的流程失败", e);
        }
    }

    /**
     * 限时审批逾期流程
     *
     * @param properties         限时审批配置参数
     * @param taskVariable       任务变量
     * @param extProcessInstance
     */
    private void overdueAutomaticApproval(Process process, LocalDateTime endDate, Properties properties,
                                          TaskVariableDTO taskVariable,
                                          ExtProcessInstance extProcessInstance
                                         ) {
        try {
            LimitTimeApprovalConfigDTO configDTO = properties.getLimitTimeApprovalConfig();
            log.debug("开始限时审批的流程,{}", JsonUtils.serialize(configDTO));
            LimitTimeApprovalDTO limitTimeApproval = getLimitTimeApproval(configDTO);
            if (ObjectUtils.isNotEmpty(limitTimeApproval) && Boolean.TRUE.equals(limitTimeApproval.getChecked())) {
                var calculatedTime = DateUtils.offsetMinute(taskVariable.getCreateTime(), this.getMinutes(limitTimeApproval.getUnit(),
                                                                                                          limitTimeApproval.getValue()));
                if (DateUtils.compare(calculatedTime, endDate) <= 0) {
                    Task task = taskService.createTaskQuery().active().taskId(taskVariable.getId()).singleResult();
                    if (Objects.isNull(task)) {
                        return;
                    }
                    if ("TRANSFER".equalsIgnoreCase(limitTimeApproval.getType())) {
                        overdueTransferApproval(task, limitTimeApproval, taskVariable);
                    } else {
                        overdueAutoPassApproval(process, limitTimeApproval, taskVariable, extProcessInstance, task);
                    }
                }
            }
        } catch (Exception e) {
            log.debug("限时审批的流程结束", e);
        }
    }

    private LimitTimeApprovalDTO getLimitTimeApproval(LimitTimeApprovalConfigDTO configDTO) {
        if (ObjectUtils.isEmpty(configDTO)) {
            return null;
        }
        if (ObjectUtils.isNotEmpty(configDTO.getLimitTimeApproval())) {
            return configDTO.getLimitTimeApproval();
        }
        if (ObjectUtils.isEmpty(configDTO.getAutoPass())) {
            return null;
        }
        LimitTimeApprovalDTO limitTimeApproval = new LimitTimeApprovalDTO();
        limitTimeApproval.setChecked(configDTO.getAutoPass().getChecked());
        limitTimeApproval.setUnit(configDTO.getAutoPass().getUnit());
        limitTimeApproval.setValue(configDTO.getAutoPass().getValue());
        limitTimeApproval.setType("PASS");
        return limitTimeApproval;
    }

    private void overdueTransferApproval(Task task, LimitTimeApprovalDTO limitTimeApproval, TaskVariableDTO taskVariable) {
        String targetAccount = limitTimeApproval.getTargetAccount();
        if (StringUtils.isBlank(targetAccount)) {
            log.warn("任务{}配置了超时转交但未配置目标审批人", task.getId());
            return;
        }
        targetAccount = Arrays.stream(targetAccount.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(","));
        if (StringUtils.isBlank(targetAccount)) {
            log.warn("任务{}配置了超时转交但未配置有效目标审批人", task.getId());
            return;
        }
        if (Arrays.stream(targetAccount.split(",")).anyMatch(taskVariable.getAssignee().toString()::equals)) {
            return;
        }
        ApprovalOpinion approvalOpinion = new ApprovalOpinion();
        approvalOpinion.setSign("");
        approvalOpinion.setText("超时自动转交");
        approvalOpinion.setApprovalType(ApprovalTypeEnum.OVERDUE_TRANSFER.name());
        commandExecutor.execute(new com.guhecloud.cube.base.flow.flowable.task.application.operation.transfer.TransferCommand(
                task,
                taskVariable.getAssignee().toString(),
                targetAccount,
                approvalOpinion
        ));
    }

    private void overdueAutoPassApproval(Process process, LimitTimeApprovalDTO limitTimeApproval, TaskVariableDTO taskVariable,
                                         ExtProcessInstance extProcessInstance, Task task
                                        ) {
        CompleteTaskCommand completeTaskCommand = this.getCompleteTaskCommand(taskVariable.getId(), taskVariable.getProcessInstanceId());
        if (ObjectUtils.isEmpty(completeTaskCommand)) {
            return;
        }
        log.debug("开始自动审批的流程,{}", JsonUtils.serialize(completeTaskCommand));

        taskService.setVariableLocal(task.getId(), VariableConstant.TASK_RESULT, completeTaskCommand.getResult());
        taskService.setVariableLocal(task.getId(), VariableConstant.APPROVAL_OPINION, completeTaskCommand.getApprovalOpinion());

        List<TaskLog.Operator> operators = Collections.singletonList(new TaskLog.Operator(taskVariable.getAssignee(), "节点审批超时,自动通过"));
        taskOperatePostProcessor.after(task, TaskOperationType.PASS.name(), operators, completeTaskCommand);

        try {
            taskService.complete(task.getId(), completeTaskCommand.getVariables());
        } catch (ActivitiException e) {
            if (e.getMessage().contains("No outgoing sequence flow of the exclusive gateway")) {
                throw new ServiceException("请先配置流程分支");
            }
        }
        NotifyUser notifyUser = new NotifyUser(taskVariable.getAssignee());
        try {
            ApplicationContextHolder.getBean(MessageSender.class)
                    .appendMessageBody("formName", extProcessInstance.getExtProcessInstancePO().getInstanceName())
                    .appendMessageBody("value", limitTimeApproval.getValue() +
                                                FlowDateUnitEnum.getByName(limitTimeApproval.getUnit()).getDescription())
                    .appendApplication(extProcessInstance.getApplicationId())
                    .appendProcessInstanceId(taskVariable.getProcessInstanceId())
                    .send("AUTOMATIC_APPROVAL", Collections.singletonList(notifyUser));

            RocketMQContextHolder.syncSendOrderly(
                    TodoTaskFlowMessage.TOPIC,
                    buildTodoMessage(task.getId(), task.getAssignee()),
                    task.getProcessInstanceId());
            sendOtherNodeTaskDeleteMessage(task, process);
        } catch (Exception e) {
            log.error("自动审批通过发送消息失败", e);
        }
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

    private void sendOtherNodeTaskDeleteMessage(Task task, Process process) {
        FlowElement currentFlowElement = process.getFlowElement(task.getTaskDefinitionKey());
        if (currentFlowElement instanceof UserTask) {
            UserTask userTask = (UserTask) currentFlowElement;
            String completionCondition = userTask.getLoopCharacteristics().getCompletionCondition();
            //只有或签节点自动通过需要处理
            if (StringUtils.isNotBlank(completionCondition) && "${nrOfCompletedInstances/nrOfInstances > 0}".equals(completionCondition)) {
                AutoPassTodoMessage build = AutoPassTodoMessage.builder()
                        .id(task.getId())
                        .taskDefinitionKey(task.getTaskDefinitionKey())
                        .processInstanceId(task.getProcessInstanceId())
                        .build();
                RocketMQContextHolder.asyncSend(AutoPassMessageTopic.TOPIC, build, 3);
            }
        }
    }

    /**
     * 将天、小时转换为分钟
     */
    private Integer getMinutes(String unit, Double value) {
        int minutes = 0;
        if (FlowDateUnitEnum.MINUTE.name().equals(unit)) {
            minutes = value.intValue();
        } else if (FlowDateUnitEnum.HOUR.name().equals(unit)) {
            minutes = (int) (value * 60);
        } else if (FlowDateUnitEnum.DAY.name().equals(unit)) {
            minutes = (int) (value * 24 * 60);
        }
        return minutes;
    }

    /**
     * 组装审批信息
     */
    private CompleteTaskCommand getCompleteTaskCommand(String taskId, String processInstanceId) {
        ExtProcessInstancePO processInstancePO = extProcessInstanceMapper.selectOne(new LambdaQueryWrapper<ExtProcessInstancePO>()
                                                                                            .eq(ExtProcessInstancePO::getId, processInstanceId)
                                                                                            .last("limit 1"));
        if (ObjectUtils.isEmpty(processInstancePO)) {
            return null;
        }
        ApprovalOpinion approvalOpinion = new ApprovalOpinion();
        approvalOpinion.setSign("");
        approvalOpinion.setText("节点审批超时,自动通过");
        approvalOpinion.setApprovalType(ApprovalTypeEnum.OVERDUE_APPROVER.name());
        JsonNode deserialize = JsonUtils.deserialize(processInstancePO.getData());
        JsonNode jsonNode = deserialize.get(deserialize.size() - 1);
        return CompleteTaskCommand.builder()
                .account(SecurityContextHolder.getPrincipal().getId().toString())
                .taskId(taskId)
                .result(FlowResult.PASS.name())
                .approvalOpinion(approvalOpinion)
                .data(JsonUtils.serialize(jsonNode))
                .build();
    }

}
