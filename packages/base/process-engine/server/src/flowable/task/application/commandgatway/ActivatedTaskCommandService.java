package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.flowable.common.ProcessHelper;
import com.guhecloud.cube.base.flow.flowable.common.ProcessUserDTO;
import com.guhecloud.cube.base.flow.flowable.common.message.MessageSender;
import com.guhecloud.cube.base.flow.flowable.common.message.NotifyUser;
import com.guhecloud.cube.base.flow.flowable.data.DataParser;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.ProcessInstanceContextBuilder;
import com.guhecloud.cube.base.flow.flowable.process.application.service.ProcessInstanceCacheService;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtHistoricProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.ProcessInstanceContext;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.BusinessId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.scene.approval.domain.enumeration.ApprovalResult;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.AddMultiInstanceCmd;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.AddMultipleSignCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.AppointApproverCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.CancellationCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.CopyCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.HastenCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.PreReduceSignCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.RejectCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.RepeatCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.TransferCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.UndoCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.CommandExecutor;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.LocalTaskOperationHandler;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.back.BackCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.pass.PassCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.preAddSign.PreAddSignCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.TaskOperatePostProcessor;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.application.querygateway.TaskInstanceQueryService;
import com.guhecloud.cube.base.flow.flowable.task.application.validator.TaskHandleValidator;
import com.guhecloud.cube.base.flow.flowable.task.domain.TaskInstanceDomainService;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import instance.command.BackTaskCommand;
import instance.command.CompleteTaskCommand;
import instance.event.NodePassMessage;
import instance.event.ProcessBackMessage;
import instance.event.ProcessWithdrawMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static base.constant.VariableConstant.ROOT;

/**
 * @author pengjian
 * @date 2022/9/1 15:09
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class ActivatedTaskCommandService {

    private final TaskService taskService;
    private final TaskInstanceQueryService taskInstanceQueryService;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final ProcessHelper helper;
    private final ManagementService managementService;
    private final TaskHandleValidator taskHandleValidator;
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final PreCompleteTask preCompleteTask;
    private final TaskOperatePostProcessor taskOperatePostProcessor;
    private final CommandExecutor commandExecutor;
    private final FileClient fileClient;
    private final ExtProcessInstanceMapper extProcessInstanceMapper;
    //审批通过消息推送配置,后续需要作为可配置项
    private static final boolean passMessageConfig = false;
    private final ProcessInstanceCacheService processInstanceCacheService;

    /**
     * 完成任务
     *
     * @param completeTaskCommand 完成任务命令
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void complete(@Valid CompleteTaskCommand completeTaskCommand) {
        Task task = preCompleteTask.precompletion(completeTaskCommand);
        //如果是根节点调用为再次发起
        if (ROOT.equals(task.getTaskDefinitionKey())) {
            this.submitAgainCompleteTask(completeTaskCommand, task);
        } else {
            commandExecutor.addLocalHandler(
                    new LocalTaskOperationHandler<PassCommand>() {
                        @Override
                        public void before(PassCommand passCommand) {
                            log.info("before---");
                        }

                        @Override
                        public void after(PassCommand passCommand) {
                            log.info("发送消息通知-------");
                            if (ApprovalResult.PASS.name().equals(completeTaskCommand.getResult()) && passMessageConfig) {
                                ProcessUserDTO processStartUser = helper.getProcessStartUser(task.getProcessInstanceId());
                                NotifyUser notifyUser = new NotifyUser(Long.parseLong(processStartUser.getId()));

                                ProcessDefinition processDefinition = ApplicationContextHolder.getBean(RepositoryService.class)
                                        .createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
                                Optional<ExtHistoricProcessInstance> extProcessInstance = extProcessInstanceRepository.getHistoric(task.getProcessInstanceId());
                                String assignee = task.getAssignee();
                                UserDTO user = ApplicationContextHolder.getBean(UserClient.class).getUser(Long.parseLong(assignee));
                                if (ObjectUtils.isNotEmpty(user)) {
                                    ApplicationContextHolder.getBean(MessageSender.class)
                                            .appendMessageBody("formName", processDefinition.getName())
                                            .appendMessageBody("approvalUser", user.getName())
                                            .appendApplication(extProcessInstance.get().getApplicationId())
                                            .appendProcessInstanceId(extProcessInstance.get().getProcessInstance().getId())
                                            .send("APPROVAL_PASSED_START_USER", Collections.singletonList(notifyUser));
                                }
                            }
                            log.info("发送rocket消息----");
                            if (!completeTaskCommand.isInnerServer()) {
                                ExecutorContextHolder.run(() -> {
                                    //传递租户信息
                                    SessionContextHolder.requestTenant(SessionContextHolder.getRequester().getTenant());
                                    sendNodePassMessage(task.getProcessInstanceId());
                                });

                            }

                        }
                    }
                                           ).execute(new PassCommand(task,
                                                                     completeTaskCommand.getResult(),
                                                                     completeTaskCommand.getData(),
                                                                     completeTaskCommand.getAccount(),
                                                                     completeTaskCommand.getVariables(),
                                                                     completeTaskCommand.getApprovalOpinion()
            ));
        }
    }

    /**
     * @Author wql
     * @Description 发送节点审批通过消息
     * @Date 14:08 2023/11/6
     **/
    private void sendNodePassMessage(String processInstanceId) {
        // 构建流程上下文
        ProcessInstanceContext processInstanceContext = ApplicationContextHolder.getBean(ProcessInstanceContextBuilder.class)
                .build(processInstanceId);
        // 发送流程完成消息
        RocketMQContextHolder.send(
                NodePassMessage.TOPIC + ":" + processInstanceContext.getExtProcessInstance().getBusinessId().getBusinessType(),
                buildNodePassMessage(processInstanceContext)
                                  );
    }

    private NodePassMessage buildNodePassMessage(ProcessInstanceContext processInstanceContext) {
        BusinessId businessId = processInstanceContext.getExtProcessInstance().getBusinessId();
        return NodePassMessage.builder()
                .data(ApplicationContextHolder.getBean(DataParser.class).parse(processInstanceContext))
                .processInstanceId(processInstanceContext.getExtProcessInstance().getProcessInstance().getId())
                .businessKey(businessId.getBusinessKey())
                .businessType(businessId.getBusinessType())
                .businessTypeName(businessId.getBusinessTypeName())
                .startTime(DateUtils.parse(processInstanceContext.getExtProcessInstance().getProcessInstance()
                                                   .getStartTime()))
                .tenant(Integer.valueOf(processInstanceContext.getExtProcessInstance().getProcessInstance().getTenantId()))
                .startUserId(Long.valueOf(processInstanceContext.getExtProcessInstance().getProcessInstance().getStartUserId()))
                .sourceProcessId(processInstanceContext.getExtProcessInstance().getSourceProcessId())
                .build();
    }

    /**
     * 驳回
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void reject(@Valid RejectCommand rejectCommand) {
        commandExecutor.execute(
                new com.guhecloud.cube.base.flow.flowable.task.application.operation.reject.RejectCommand(
                        rejectCommand.getTaskId(),
                        rejectCommand.getAccount(),
                        rejectCommand.getComment(),
                        rejectCommand.getData(),
                        rejectCommand.getRejectStrategyEnum(),
                        rejectCommand.getSelectNode(),
                        rejectCommand.getTerminate(),
                        rejectCommand.getApprovalOpinion()
                )
                               );
    }

    @Transactional(rollbackFor = Exception.class)
    public void copy(CopyCommand copyCommand) {
        commandExecutor.execute(new com.guhecloud.cube.base.flow.flowable.task.application.operation.copy.CopyCommand(
                copyCommand.getTaskKey(),
                copyCommand.getTaskName(),
                copyCommand.getProcessInstanceId(),
                copyCommand.getCopyUsers()
        ));
    }

    /**
     * 再次发起
     * 重新启动一个流程而不是在原有流程上叠加
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void repeat(RepeatCommand repeatCommand) {
        Task task = taskService.createTaskQuery().taskId(repeatCommand.getProcessInstanceId()).singleResult();
        if (Objects.nonNull(task)) {
            repeatCommand.setProcessInstanceId(task.getProcessInstanceId());
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repeatCommand.getProcessInstanceId()).singleResult();
        if (Objects.nonNull(processInstance)) {
            throw new ServiceException("流程进行中,不能再次发起");
        }

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(repeatCommand.getProcessInstanceId()).singleResult();
        // 只有启动用户才能撤销
        if (!repeatCommand.getAccount().equals(historicProcessInstance.getStartUserId())) {
            throw new ServiceException("发起人才能撤销");
        }

        commandExecutor.execute(new com.guhecloud.cube.base.flow.flowable.task.application.operation.repeat.RepeatCommand(
                repeatCommand.getProcessInstanceId(),
                repeatCommand.getAccount(),
                repeatCommand.getData(),
                repeatCommand.getComment(),
                repeatCommand.getVariables()
        ));
    }

    /**
     * 加签
     *
     * @param addMultipleSignCommand 加签命令
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void addMultipleSign(AddMultipleSignCommand addMultipleSignCommand) {
        // 校验
        taskHandleValidator.attributionVerify(addMultipleSignCommand.getTaskId(), addMultipleSignCommand.getAccount());

        // 加签（多实例）
        for (String account : addMultipleSignCommand.getAddSignAccounts()) {
            managementService.executeCommand(new AddMultiInstanceCmd(addMultipleSignCommand.getTaskId(), account)
                                            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public synchronized void cancellation(String taskId, String account, String comment) {
        Task task = taskHandleValidator.attributionVerify(taskId, account);
        this.cancellation(new CancellationCommand(task.getProcessInstanceId(), account, comment));
    }

    /**
     * 作废
     *
     * @param cancellationCommand 作废命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancellation(CancellationCommand cancellationCommand) {
        // 校验
        runtimeService.deleteProcessInstance(cancellationCommand.getProcessInstanceId(), cancellationCommand.getComment());
    }

    /**
     * 催办
     *
     * @param hastenCommand 催办命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void hasten(HastenCommand hastenCommand) {

        // 获取催办用户
        Set<Long> taskAssignees = taskInstanceQueryService.getActivatedTaskAssignees(hastenCommand.getProcessInstanceId());

        List<NotifyUser> notifyUsers = new ArrayList<>();

        taskAssignees.forEach(taskAssignee -> notifyUsers.add(new NotifyUser(taskAssignee)));

        // 催办消息发送
    }

    /**
     * 退回
     *
     * @param backCommand 回退命令
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized ProcessBackMessage back(BackTaskCommand backCommand) {
        //任务归属校验
        Task task = taskHandleValidator.attributionVerify(backCommand.getTaskId(),
                                                          backCommand.getAccount());

        boolean isFirstArtificialTask = TaskInstanceDomainService.isFirstArtificialTask(task);
        if (isFirstArtificialTask) {
            throw new ServiceException("第一个审批节点不支持回退");
        }
        commandExecutor.execute(new BackCommand(
                backCommand.getAccount(),
                task,
                backCommand.getComment(),
                backCommand.getVariables(),
                backCommand.getApprovalOpinion(),
                TaskOperationType.BACK
        ));
        ExtProcessInstancePO extProcessInstancePO = extProcessInstanceMapper.selectById(task.getProcessInstanceId());
        if (Objects.isNull(extProcessInstancePO)) {
            return null;
        }
        Object sourceProcessId = task.getTaskLocalVariables().get(VariableConstant.SOURCE_PROCESS_ID);
        return ProcessBackMessage.builder()
                .startUserId(extProcessInstancePO.getStartUserId())
                .processInstanceId(extProcessInstancePO.getId())
                .businessType(extProcessInstancePO.getBusinessType())
                .sourceProcessId(Objects.nonNull(sourceProcessId) ? sourceProcessId.toString() : null)
                .tenant(SessionContextHolder.getRequester().getTenant())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public synchronized void undo(String processInstanceId, String account, String comment) {
        undo(new UndoCommand(account, processInstanceId, comment));

    }

    /**
     * 撤销 流程进行中才能撤销
     *
     * @param undoCommand 撤销命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void undo(UndoCommand undoCommand) {
        Task singleResult = taskService.createTaskQuery().taskId(undoCommand.getProcessInstanceId()).singleResult();
        if (Objects.nonNull(singleResult)) {
            undoCommand.setProcessInstanceId(singleResult.getProcessInstanceId());
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().includeProcessVariables()
                .processInstanceId(undoCommand.getProcessInstanceId()).singleResult();
        if (Objects.isNull(processInstance)) {
            throw new ServiceException("流程进行中才能撤销");
        }

        // 只有启动用户才能撤销
        if (!undoCommand.getAccount().equals(processInstance.getStartUserId())) {
            throw new ServiceException("发起人才能撤销");
        }

        commandExecutor.execute(new com.guhecloud.cube.base.flow.flowable.task.application.operation.undo.UndoCommand(
                undoCommand.getAccount(),
                undoCommand.getProcessInstanceId(),
                undoCommand.getComment()
        ));
    }

    /**
     * 转交
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void transfer(TransferCommand transferCommand) {
        Task task = taskHandleValidator.attributionVerify(transferCommand.getTaskId(), transferCommand.getAccount());
        commandExecutor.execute(new com.guhecloud.cube.base.flow.flowable.task.application.operation.transfer.TransferCommand(
                task,
                transferCommand.getAccount(),
                transferCommand.getTargetAccount(),
                transferCommand.getApprovalOpinion()
        ));

    }

    @Transactional(rollbackFor = Exception.class)
    public void submitAgainCompleteTask(CompleteTaskCommand passCommand, Task task) {
        Optional<ExtProcessInstance> optional = extProcessInstanceRepository.get(task.getProcessInstanceId());
        optional.ifPresent(extProcessInstance -> {
            extProcessInstance.getDataMap().put(task.getTaskDefinitionKey(), passCommand.getData());
            runtimeService.setVariable(extProcessInstance.getProcessInstance().getId(), VariableConstant.RESULT, passCommand.getResult());
            extProcessInstanceRepository.save(extProcessInstance);
            StartUserInfo startUserInfo = extProcessInstance.getStartUserInfo();
            taskOperatePostProcessor.after(task, TaskOperationType.REPEAT.name(), Collections.singletonList(new TaskLog.Operator(startUserInfo.getStartUserId(),
                                                                                                                                 startUserInfo.getStartUserName())), ApprovalOpinionSupport.EMPTY);
        });
        try {
            taskService.complete(task.getId(), passCommand.getVariables(), true);
        } catch (ActivitiException e) {
            if (e.getMessage().contains("No outgoing sequence flow of the exclusive gateway")) {
                throw new ServiceException("请先配置流程分支");
            }
        }

    }

    /**
     * 预减签
     */
    @Transactional(rollbackFor = Exception.class)
    public void preReduceSign(PreReduceSignCommand preReduceSignCommand) {
        // 兼容
        HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery()
                .taskId(preReduceSignCommand.getProcessInstanceId()).singleResult();
        if (Objects.nonNull(taskInstance)) {
            preReduceSignCommand.setProcessInstanceId(taskInstance.getProcessInstanceId());
        }
        PreReduceSignChecker preReduceSignChecker = ApplicationContextHolder.getBean(PreReduceSignChecker.class);
        preReduceSignChecker.preCheck(preReduceSignCommand);

        // 只能减临时加的，不能减流程原有配置的人员
        commandExecutor.execute(new com.guhecloud.cube.base.flow.flowable.task.application.operation.preReduceSign.PreReduceSignCommand(
                preReduceSignCommand.getProcessInstanceId(),
                preReduceSignCommand.getTaskKey(),
                preReduceSignCommand.getUserIds()
        ));
    }

    /**
     * 指定审批人
     * 还没有创建任务的时候加签
     * 删除加签只能删除自己加的人
     * 流程配置好的不能减签
     * <p>
     * 只有当前节点的人员并且开启了配置，这个节点的人才能加签
     */
    @Transactional(rollbackFor = Exception.class)
    public void preAddMultipleSign(AppointApproverCommand appointApproverCommand) {
        // 兼容
        HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery()
                .taskId(appointApproverCommand.getProcessInstanceId()).singleResult();
        if (Objects.nonNull(taskInstance)) {
            appointApproverCommand.setProcessInstanceId(taskInstance.getProcessInstanceId());
        }

        PreAddSignChecker preAddSignChecker = ApplicationContextHolder.getBean(PreAddSignChecker.class);
        preAddSignChecker.preCheck(appointApproverCommand);

        commandExecutor.execute(new PreAddSignCommand(
                appointApproverCommand.getProcessInstanceId(),
                appointApproverCommand.getTaskKey(),
                appointApproverCommand.getUserIds()
        ));
    }

    public FileVO getAssignee(Long assigneeId) {
        FileVO fileVO = new FileVO();
        String approvalOpinionStr = extProcessInstanceMapper.selectHistoryUserLastSign(assigneeId);
        if (StringUtils.isBlank(approvalOpinionStr)) {
            return null;
        }
        try {
            ApprovalOpinion approvalOpinion = JsonUtils.deserialize(approvalOpinionStr, ApprovalOpinion.class);
            fileVO = fileClient.getById(Long.valueOf(approvalOpinion.getSign()));
            String fileBase64 = fileClient.getFileBase64(fileVO.getId());
            if (StringUtils.isBlank(fileBase64)) {
                return null;
            }
        } catch (Exception exception) {
            log.error("获取上次签名失败，文件不存在");
        }
        return fileVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public ProcessWithdrawMessage withdrawTask(BackTaskCommand backCommand) {
        Task task = helper.checkTask(backCommand.getTaskId());
        ProcessWithdrawMessage message = new ProcessWithdrawMessage();
        Boolean flag = processInstanceCacheService.withdrawNodePreCheck(task.getProcessInstanceId(), SecurityContextHolder.getPrincipal()
                .getId(), message);
        taskService.setAssignee(task.getId(), SecurityContextHolder.getPrincipal().getId().toString());
        Assert.isNotAcceptable(!flag, "节点已处理，撤回失败");
        commandExecutor.execute(new BackCommand(
                backCommand.getAccount(),
                task,
                backCommand.getComment(),
                backCommand.getVariables(),
                backCommand.getApprovalOpinion(),
                TaskOperationType.WITHDRAW
        ));
        ExtProcessInstancePO extProcessInstancePO = extProcessInstanceMapper.selectById(task.getProcessInstanceId());
        message.setBusinessType(extProcessInstancePO != null ? extProcessInstancePO.getBusinessType() : "");
        return message;
    }
}
