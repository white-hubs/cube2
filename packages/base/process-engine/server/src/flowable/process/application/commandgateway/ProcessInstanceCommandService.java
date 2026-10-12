package com.guhecloud.cube.base.flow.flowable.process.application.commandgateway;

import base.constant.VariableConstant;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.context.submit.commandgateway.command.ProcessStartedMessageAttrCommand;
import com.guhecloud.cube.base.flow.context.submit.interfaces.StartAfterProcessInstanceCallBackData;
import com.guhecloud.cube.base.flow.context.tool.CallBackEvent;
import com.guhecloud.cube.base.flow.flowable.common.ProcessHelper;
import com.guhecloud.cube.base.flow.flowable.common.message.MessageSender;
import com.guhecloud.cube.base.flow.flowable.common.message.NotifyUser;
import com.guhecloud.cube.base.flow.flowable.data.DataParser;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.command.CloseProcessInstanceCommand;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.command.StartProcessInstanceCommand;
import com.guhecloud.cube.base.flow.flowable.process.application.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.flowable.process.application.event.publisher.ProcessCompletedMessage;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.ProcessInstanceQueryService;
import com.guhecloud.cube.base.flow.flowable.process.application.service.ProcessInstanceService;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstanceDomainService;
import com.guhecloud.cube.base.flow.flowable.process.domain.ProcessInstanceContext;
import com.guhecloud.cube.base.flow.flowable.process.domain.factory.ExtProcessInstanceFactory;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.BusinessId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessExecuteContext;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.TaskOperatePostProcessor;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskLifeCycle;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.webhooks.rocketmq.WebHookMessage;
import com.guhecloud.cube.base.message2.client.SatSendClient;
import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgChannelSendParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgSendParam;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQueryService;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskDelMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskDelMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
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
import instance.event.ProcessStartedMessage;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static base.constant.VariableConstant.TEMPORARY;
import static com.guhecloud.cube.base.flow.flowable.common.ProcessService.TASK_RESULT_CLOSED;

/**
 * @author pengjian
 * @date 2022/8/29 11:00
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = Exception.class)
public class ProcessInstanceCommandService {
    public static final String RISK_ERROR_MESSAGE = "streamengineH5/public-process/?processType=HANDLING&businessType=%s&processId=%s&tenant=%s";
    private final ProcessInstanceService processInstanceService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final ProcessHelper helper;
    private final ProcessInstanceQueryService processInstanceQueryService;
    @Inject
    private ExtProcessInstanceDomainService extProcessInstanceDomainService;
    private final TaskOperatePostProcessor taskOperatePostProcessor;
    private final UserQueryService userQueryService;
    private final CallBackEvent callBackEvent;
    private final HistoryService historyService;
    private final MessageSender messageSender;
    private final SatSendClient satSendClient;
    private final ExtProcessInstanceRepository extProcessInstanceRepository;

    /**
     * 启动流程实例
     */
    public ProcessInstanceDTO start(final StartProcessInstanceCommand startProcessInstanceCommand) {

        // 历史包袱，后续优化
        UserDTO userDTO;
        if (startProcessInstanceCommand.isPublicForm()) {
            userDTO = new UserDTO();
            userDTO.setId(Long.valueOf(startProcessInstanceCommand.getStartAccount()));
            userDTO.setName(startProcessInstanceCommand.getSubmitName());
        } else {
            userDTO = userQueryService.userIds(Collections.singleton(Long.valueOf(startProcessInstanceCommand.getStartAccount())))
                    .singleResult();
            if (Objects.isNull(userDTO)) {
                userDTO = new UserDTO();
                userDTO.setName(SecurityContextHolder.getPrincipal().getName());
                userDTO.setId(SecurityContextHolder.getPrincipal().getId());
            }
        }
        // 准备工作
        prepareRefresh(startProcessInstanceCommand, userDTO);

        // 创建流程实例
        ProcessInstanceDTO processInstanceDTO = createInstance(startProcessInstanceCommand, userDTO);

        // 流程实例启动成功  执行回调
        finishRefresh(processInstanceDTO, startProcessInstanceCommand.getBusinessId(), startProcessInstanceCommand.getAppId(), startProcessInstanceCommand.getVariables());
        //外部人员发起后发送消息
        if (startProcessInstanceCommand.isPublicForm()) {
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("businessTypeName", startProcessInstanceCommand.getBusinessId().getBusinessTypeName());
            msgParams.put("url", String.format(RISK_ERROR_MESSAGE,
                                               startProcessInstanceCommand.getBusinessId().getBusinessType(),
                                               processInstanceDTO.getId(),
                                               SessionContextHolder.getRequester().getTenant()));
            List<SatMsgChannelSendParam> satMsgChannelSendParams = new ArrayList<>();
            MsgSendTargetParam sendTargetParam = MsgSendTargetParam.builder().userId(userDTO.getId()).userName(userDTO.getUsername())
                    .userPhone(userDTO.getMobile()).receiver(userDTO.getId().toString()).build();
            satMsgChannelSendParams.add(new SatMsgChannelSendParam(null, ChannelEnum.CHANNEL_SMS, SessionContextHolder.getRequester()
                    .getTenant(), Collections.singletonList(sendTargetParam)));
            try {
                this.satSendClient.sendMsg(SatMsgSendParam.builder().businessId(processInstanceDTO.getId())
                                                   .applicationCode(startProcessInstanceCommand.getAppId().getApplicationCode())
                                                   .applicationName(startProcessInstanceCommand.getAppId().getApplicationName())
                                                   .msgSceneCode("OUTSIDER_APPLY_START_USER").msgParams(msgParams)
                                                   .satMsgChannelSendParams(satMsgChannelSendParams)
                                                   .tenant(SessionContextHolder.getRequester().getTenant()).build());
                log.info("消息:{},发送成功", "OUTSIDER_APPLY_START_USER");
            } catch (Exception e) {
                log.error("消息发送失败:{}", e.getMessage());
            }

        }
        return processInstanceDTO;
    }

    /**
     * @Author wql
     * @Description 设置发起人指定的审批人信息
     * @Date 9:54 2023/5/16
     **/
    private void setApproveUser(ProcessInstanceDTO processInstanceDTO, Map<String, String> approveUsers) {
        if (ObjectUtils.isEmpty(approveUsers)) {
            return;
        }
        String processInstanceId = processInstanceDTO.getId();
        approveUsers.forEach((key, value) -> {
            if (StringUtils.isNotBlank(value)) {
                String variable = key + "assigneeList";
                List<String> users = Arrays.stream(value.split(",")).distinct().collect(Collectors.toList());
                runtimeService.setVariable(processInstanceId, TEMPORARY + variable, users);
            }
        });
    }

    /**
     * 准备工作
     */
    private void prepareRefresh(StartProcessInstanceCommand startProcessInstanceCommand, UserDTO userDTO) {
        startProcessInstanceCommand.getVariables().put("root", startProcessInstanceCommand.getStartAccount());
        startProcessInstanceCommand.getVariables().put(VariableConstant.START_USER_INFO, JsonUtils.serialize(userDTO));
        startProcessInstanceCommand.getVariables().put(VariableConstant.DATA, startProcessInstanceCommand.getData());
        startProcessInstanceCommand.getVariables().put(VariableConstant.FORM_INSTANCE_Id, startProcessInstanceCommand.getFormInstanceId());
        if (StringUtils.isNotBlank(startProcessInstanceCommand.getRemark())) {
            startProcessInstanceCommand.getVariables().put(VariableConstant.REMARK, startProcessInstanceCommand.getRemark());
        }
        if (startProcessInstanceCommand.isPublicForm()) {
            startProcessInstanceCommand.getVariables().put(VariableConstant.OutSider, userDTO.getId());
        }
        // 预提交回调
        Object preResponse = callBackEvent.beforeCallBack(startProcessInstanceCommand);
        if (Objects.nonNull(preResponse)) {
            startProcessInstanceCommand.getVariables().put(VariableConstant.PRE_SUBMIT_RESPONSE, preResponse);
        }
        startProcessInstanceCommand.getVariables().put("assigneeName", userDTO.getId());
    }

    private ProcessInstanceDTO createInstance(StartProcessInstanceCommand startProcessInstanceCommand, UserDTO userDTO) {
        ProcessInstanceDTO processInstanceDTO = processInstanceService.startProcessInstance(
                startProcessInstanceCommand.getStartAccount(),
                startProcessInstanceCommand.getProcessDefinitionKey(),
                startProcessInstanceCommand.getBusinessId().getBusinessType(),
                startProcessInstanceCommand.getBusinessId().getBusinessKey(),
                startProcessInstanceCommand.getVariables());
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceDTO.getId()).includeProcessVariables().singleResult();

        StartUserInfo startUserInfo = new StartUserInfo(
                userDTO.getId(),
                userDTO.getName(),
                Optional.ofNullable(userDTO.getOrganization()).orElse(new UserDTO.Organization()).getId(),
                Optional.ofNullable(userDTO.getOrganization()).orElse(new UserDTO.Organization()).getName(),
                userDTO.getMobile()
        );
        ExtProcessInstance extProcessInstance = ExtProcessInstanceFactory.create(processInstance,
                                                                                 startProcessInstanceCommand.getBusinessId(),
                                                                                 startProcessInstanceCommand.getAppId(),
                                                                                 startProcessInstanceCommand.getVariables()
                                                                                         .get(VariableConstant.DATA)
                                                                                         .toString(), startUserInfo);

        extProcessInstance.setInstanceName(startProcessInstanceCommand.getInstanceName());

        extProcessInstanceDomainService.save(extProcessInstance);
        ProcessExecuteContext.initInfo(extProcessInstance);
        setApproveUser(processInstanceDTO, startProcessInstanceCommand.getApproveUsers());

        completeFirstTask(extProcessInstance, startProcessInstanceCommand.isPublicForm());

        return processInstanceDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void completeFirstTask(ExtProcessInstance processInstance, boolean publicForm) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstance().getProcessInstanceId()).active()
                .singleResult();
        task.setAssignee(processInstance.getProcessInstance().getStartUserId());
        taskService.claim(task.getId(), processInstance.getProcessInstance().getStartUserId());
        Map<String, Object> map = new HashMap<>();
        map.put(task.getTaskDefinitionKey(), processInstance.getProcessInstance().getProcessVariables().get(VariableConstant.DATA));
        processInstance.setDataMap(map);
        extProcessInstanceRepository.save(processInstance);
        taskOperatePostProcessor.after(task, TaskOperationType.SUBMIT.name(), processInstance, ApprovalOpinionSupport.EMPTY);

        try {
            taskService.complete(task.getId());
        } catch (ActivitiException e) {
            if (e.getMessage().contains("No outgoing sequence flow of the exclusive gateway")) {
                endProcessInstance(TaskOperationType.ABNORMAL, processInstance.getProcessInstance()
                        .getProcessInstanceId(), TaskOperationType.ABNORMAL.name());
                throw new ServiceException("请先配置流程分支");
            }
        }

    }

    /**
     * 流程实例启动完成
     */
    private void finishRefresh(ProcessInstanceDTO processInstanceDTO, BusinessId businessId, ApplicationId applicationId,
                               Map<String, Object> variables
                              ) {
        try {
            // 生成TaskContext,未来会生成ProcessInstanceContext;
            callBackEvent.afterCallBack(
                    new StartAfterProcessInstanceCallBackData(
                            processInstanceDTO.getId(),
                            variables.get(VariableConstant.PRE_SUBMIT_RESPONSE),
                            businessId.getBusinessType(),
                            applicationId.getApplicationCode(),
                            ""
                    )
                                       );
            //流程发起成功后发送rocket消息
            sendStartSuccessMessage(processInstanceDTO.getId());
        } catch (Exception e) {
            log.error("流程后置处理异常", e);
            // 流程后置处理异常，删除待办任务
            String processInstanceDTOId = processInstanceDTO.getId();
            List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceDTOId).list();
            for (Task task : list) {
                TodoTaskDelMessage todoTaskDelMessage = new TodoTaskDelMessage();
                todoTaskDelMessage.setBusinessKey(task.getId());
                todoTaskDelMessage.setOperationType(TodoTaskFlowOperationEnum.DELETE);
                RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC, todoTaskDelMessage, task.getProcessInstanceId());

                WorkTaskDelMessage workTaskDelMessage = new WorkTaskDelMessage();
                workTaskDelMessage.setBusinessKey(task.getId());
                workTaskDelMessage.setOperationType(WorkTaskFlowOperationEnum.DELETE);
                RocketMQContextHolder.syncSendOrderly(WorkTaskDelMessage.TOPIC, workTaskDelMessage, task.getProcessInstanceId());
            }
            throw new ServiceException("流程后置处理异常");
        }
    }

    /**
     * @Author wql
     * @Description 流程发起成功后发送rocket消息
     * @Date 17:14 2023/9/14
     **/
    private void sendStartSuccessMessage(String processInstanceId) {
        // 构建流程上下文
        ProcessInstanceContext processInstanceContext = ApplicationContextHolder.getBean(ProcessInstanceContextBuilder.class)
                .build(processInstanceId);
        // 发送流程发起成功消息
        RocketMQContextHolder.send(
                ProcessStartedMessage.TOPIC + ":" + processInstanceContext.getExtProcessInstance().getBusinessId().getBusinessType(),
                buildStartSuccessMessage(processInstanceContext)
                                  );
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        list = list.stream().filter(t -> !VariableConstant.ROOT.equals(t.getTaskDefinitionKey()))
                .collect(Collectors.toList());
        if (FlowResult.PASS.name().equals(processInstanceContext.getResult()) || CollectionUtils.isEmpty(list)) {
            //发起即结束时需要更新流程状态并发送消息
            endProcessInstance(TaskOperationType.PASS, processInstanceId, FlowResult.PASS.name());
        }
    }

    private ProcessStartedMessageAttrCommand buildStartSuccessMessage(ProcessInstanceContext processInstanceContext) {
        if (Objects.isNull(processInstanceContext) || Objects.isNull(processInstanceContext.getExtProcessInstance())) {
            return null;
        }
        BusinessId businessId = processInstanceContext.getExtProcessInstance().getBusinessId();
        ProcessStartedMessageAttrCommand build = new ProcessStartedMessageAttrCommand();
        build.setData(ApplicationContextHolder.getBean(DataParser.class).parse(processInstanceContext));
        build.setProcessInstanceId(processInstanceContext.getExtProcessInstance().getProcessInstance().getId());
        build.setBusinessKey(businessId.getBusinessKey());
        build.setBusinessType(businessId.getBusinessType());
        build.setBusinessTypeName(businessId.getBusinessTypeName());
        build.setStartTime(DateUtils.parse(processInstanceContext.getExtProcessInstance().getProcessInstance()
                                                   .getStartTime()));
        build.setTenant(Integer.valueOf(processInstanceContext.getExtProcessInstance().getProcessInstance().getTenantId()));
        build.setStartUserId(Long.valueOf(processInstanceContext.getExtProcessInstance().getProcessInstance().getStartUserId()));
        build.setProcessVariables(processInstanceContext.getExtProcessInstance().getProcessInstance().getProcessVariables());
        build.setOutsiderName(processInstanceContext.getExtProcessInstance().getStartUserInfo().getStartUserName());
        build.setOutsiderPhone(processInstanceContext.getExtProcessInstance().getStartUserInfo().getStartUserId());
        build.setInstanceName(processInstanceContext.getExtProcessInstance().getProcessInstanceName());
        build.setApplicationCode(processInstanceContext.getExtProcessInstance().getApplicationId().getApplicationCode());
        build.setApplicationName(processInstanceContext.getExtProcessInstance().getApplicationId().getApplicationName());
        return build;
    }

    /**
     * 关闭流程
     */
    public void close(CloseProcessInstanceCommand closeProcessInstanceCommand) {

        Optional<ExtProcessInstance> optional = processInstanceQueryService.getByIdOrBusinessKey(closeProcessInstanceCommand.getProcessInstanceId(),
                                                                                                 closeProcessInstanceCommand.getBusinessKey());
        ExtProcessInstance processInstance = optional.orElseThrow(() -> new ServiceException("流程不存在"));

        // 删除所有任务本地变量、设置任务处理结果变量
        taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstance().getId()).list().stream().map(TaskInfo::getId)
                .forEach(taskId -> {
                    helper.clearTaskVariablesLocal(taskId);
                    helper.setResultTaskVariablesLocal(taskId, closeProcessInstanceCommand.getComment(), TASK_RESULT_CLOSED);
                });
        // 删除流程实例
        runtimeService.deleteProcessInstance(processInstance.getProcessInstance().getId(), TASK_RESULT_CLOSED);
    }

    /**
     * 结束实例
     *
     * @param processInstanceId 流程实例id
     * @param result            结果
     */
    @Transactional(rollbackFor = Exception.class)
    public void endProcessInstance(TaskOperationType taskOperationType, String processInstanceId, String result) {
        // 更新流程实例结果
        updateProcessInstanceResult(processInstanceId, result);
        // 发送待办消息
        sendTodoMessage(processInstanceId);
        // 发送rocket消息
        sendMqMessage(taskOperationType, processInstanceId);
        // 发送流程完成消息
        RocketMQContextHolder.send(WebHookMessage.TOPIC, WebHookMessage.builder()
                                           .processInstanceId(processInstanceId)
                                           .result(result)
                                           .tenant(SessionContextHolder.getRequester().getTenant())
                                           .build()
                                  );
    }

    /**
     * 发送mq消息通知流程结果
     *
     * @param taskOperationType 操作结果
     * @param processInstanceId 流程实例id
     */
    public void sendMqMessage(TaskOperationType taskOperationType, String processInstanceId) {
        // 构建流程上下文
        ProcessInstanceContext processInstanceContext = ApplicationContextHolder.getBean(ProcessInstanceContextBuilder.class)
                .build(processInstanceId);
        // 发送流程完成消息
        RocketMQContextHolder.send(
                ProcessCompletedMessage.TOPIC + ":" + processInstanceContext.getExtProcessInstance().getBusinessId().getBusinessType(),
                buildProcessCompleteMessage(processInstanceContext, taskOperationType)
                                  );
    }

    /**
     * @Author wql
     * @Description 流程结束通知发起人
     * @Date 18:04 2023/10/24
     **/
    public void notifyFlowStartUser(String processInstanceId) {
        Optional<ExtProcessInstance> extProcessInstance = extProcessInstanceRepository.get(processInstanceId);
        extProcessInstance.ifPresent(instance -> {
            messageSender
                    .appendMessageBody("formName", instance.getBusinessId().getBusinessTypeName())
                    .appendApplication(instance.getApplicationId())
                    .appendProcessInstanceId(instance.getProcessInstance().getProcessInstanceId())
                    .send("TASK_END", Collections.singletonList(new NotifyUser(instance.getStartUserInfo().getStartUserId())));
        });
    }

    private ProcessCompletedMessage buildProcessCompleteMessage(ProcessInstanceContext processInstanceContext,
                                                                TaskOperationType taskOperationType
                                                               ) {
        String data = ApplicationContextHolder.getBean(DataParser.class).parse(processInstanceContext);
        String dataSource = processInstanceContext.getExtProcessInstance().getData();
        Object preSubmitResponse = processInstanceContext.getExtProcessInstance().getProcessInstance().getProcessVariables()
                .get(VariableConstant.PRE_SUBMIT_RESPONSE);
        Map<String, Object> properties = new HashMap<>();
        properties.put(VariableConstant.getVariableName(VariableConstant.PRE_SUBMIT_RESPONSE), preSubmitResponse);
        return ProcessCompletedMessage.builder()
                .processInstanceId(processInstanceContext.getExtProcessInstance().getProcessInstance().getId())
                .result(taskOperationType.name())
                .businessId(processInstanceContext.getExtProcessInstance().getBusinessId())
                .data(data)
                .sourceData(dataSource)
                .startTime(DateUtils.parse(processInstanceContext.getExtProcessInstance().getProcessInstance().getStartTime()))
                .completedTime(LocalDateTime.now())
                .tenant(Integer.valueOf(processInstanceContext.getExtProcessInstance().getProcessInstance().getTenantId()))
                .startUserId(Long.valueOf(processInstanceContext.getExtProcessInstance().getProcessInstance().getStartUserId()))
                .mobile(processInstanceContext.getExtProcessInstance().getStartUserInfo().getMobile())
                .sourceProcessId(processInstanceContext.getExtProcessInstance().getSourceProcessId())
                .properties(properties)
                .build();
    }

    private void updateProcessInstanceResult(String processInstanceId, String result) {
        ExtProcessInstanceMapper extProcessInstanceMapper = ApplicationContextHolder.getBean(ExtProcessInstanceMapper.class);
        ExtProcessInstancePO extProcessInstancePO = extProcessInstanceMapper.selectById(processInstanceId);
        extProcessInstancePO.setResult(result);
        extProcessInstanceMapper.updateById(extProcessInstancePO);
    }

    private void sendTodoMessage(String processInstanceId) {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).list();
        List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned(SecurityContextHolder.getPrincipal().getId().toString())
                .processInstanceId(processInstanceId).active().list();
        String currentTaskId = null;
        if (CollectionUtils.isNotEmpty(list)) {
            Task task = list.getFirst();
            currentTaskId = task.getId();
        }
        final String key = currentTaskId;
        historicTaskInstances.forEach(historicTaskInstance -> {
            //发消息
            RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC, buildTodoMessage(historicTaskInstance, key), processInstanceId);
        });
        // 工作中心处理
        Set<Long> userIds = historicTaskInstances.stream().filter(t -> Objects.nonNull(t.getEndTime()) && Objects.equals(key, t.getId()))
                .map(t -> Long.valueOf(t.getAssignee())).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(userIds)) {
            WorkTaskFlowCirculationMessage message = new WorkTaskFlowCirculationMessage();
            message.setBusinessKey(processInstanceId);
            message.setBusinessStatus(TaskLifeCycle.ACTIVED.name());
            message.setUserIds(userIds);
            message.setHandlerTime(LocalDateTime.now());
            message.setGeneratedNextStatusTask(false);
            message.setNextBusinessStatus(TaskLifeCycle.COMPLEATED.name());
            message.setNextBusinessStatusName(TaskLifeCycle.COMPLEATED.getLabel());
            message.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
            RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, message, processInstanceId);
        }

    }

    private TodoTaskFlowCirculationMessage buildTodoMessage(HistoricTaskInstance historicTaskInstance, String key) {
        TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
        message.setBusinessKey(historicTaskInstance.getId());
        message.setBusinessStatus(TaskLifeCycle.ACTIVED.name());
        LocalDateTime endTime = DateUtils.parse(historicTaskInstance.getEndTime());
        if (Objects.nonNull(endTime) || (Objects.nonNull(key) && key.equals(historicTaskInstance.getId()))) {
            message.setUserIds(Collections.singleton(Long.parseLong(historicTaskInstance.getAssignee())));
        }
        message.setHandlerTime(LocalDateTime.now());
        message.setGeneratedNextStatusTask(false);
        message.setNextBusinessStatus(TaskLifeCycle.COMPLEATED.name());
        message.setNextBusinessStatusName(TaskLifeCycle.COMPLEATED.getLabel());
        message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
        return message;
    }

    public ProcessStartedMessageAttrCommand getProcessInstanceFormInfo(String processInstanceId) {
        // 构建流程上下文
        ProcessInstanceContext processInstanceContext = ApplicationContextHolder.getBean(ProcessInstanceContextBuilder.class)
                .build(processInstanceId);
        // 发送流程完成消息
        return buildStartSuccessMessage(processInstanceContext);
    }
}
