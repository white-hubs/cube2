package com.guhecloud.cube.base.flow.flowable.task.interfaces;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.flow.client.dto.TaskVariableDTO;
import com.guhecloud.cube.base.flow.flowable.common.ProcessHelper;
import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.application.querygateway.ProcessDefinitionQueryService;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.ProcessInstanceQueryService;
import com.guhecloud.cube.base.flow.flowable.process.application.service.ProcessInstanceCacheService;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.AddMultipleSignCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.AppointApproverCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.HastenCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.PreReduceSignCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.RejectCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.RepeatCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.TransferCommand;
import com.guhecloud.cube.base.flow.flowable.task.application.dto.CurrentUserTaskDTO;
import com.guhecloud.cube.base.flow.flowable.task.application.dto.ProcessInstanceSimpleDTO;
import com.guhecloud.cube.base.flow.flowable.task.domain.TaskInstance;
import com.guhecloud.cube.base.flow.flowable.task.domain.TaskInstanceDomainService;
import com.guhecloud.cube.base.flow.flowable.task.domain.repository.TaskInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskRuNodeMapper;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.CancellationCurrentProcessInstanceDTO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.ProcessIdBySourceDTO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskInfoDTO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskInfoWithBusinessTypeQuery;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskInstanceDetailDTO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.UndoCurrentProcessInstanceDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import instance.command.BackTaskCommand;
import instance.command.CompleteTaskCommand;
import instance.event.ProcessBackMessage;
import instance.event.ProcessWithdrawMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/8/22 13:58
 * @description 激活的任务
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivatedTaskController {
    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final ProcessHelper helper;
    private final ActivatedTaskCommandService activatedTaskCommandService;
    private final TaskInstanceRepository taskInstanceRepository;
    private final ProcessInstanceQueryService processInstanceQueryService;
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final HistoryService historyService;
    private final ProcessDefinitionQueryService processDefinitionQueryService;
    private final RuntimeService runtimeService;
    private final ProcessInstanceCacheService processInstanceCacheService;
    private final TaskRuNodeMapper taskRuNodeMapper;

    /**
     * 分页查询用户的激活任务
     */
    @GetMapping("/process/task/user/activated/page")
    public List<CurrentUserTaskDTO> page(String processDefinitionKey, String account) {

        String tenantId = String.valueOf(SessionContextHolder.getRequester().getTenant());
        List<CurrentUserTaskDTO> currentUserTasks = Collections.synchronizedList(new ArrayList<>());
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskTenantId(tenantId);
        if (StringUtils.isNotEmpty(processDefinitionKey)) {
            taskQuery.processDefinitionKey(processDefinitionKey);
        }
        if (StringUtils.isNotEmpty(account)) {
            taskQuery.taskCandidateOrAssigned(account);
        }

        taskQuery.orderByTaskCreateTime().desc();
        List<Task> tasks = taskQuery.list();

        tasks.forEach(task -> {
            // 流程实例
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            ProcessInstanceSimpleDTO processInstanceSimpleDTO = new ProcessInstanceSimpleDTO();
            processInstanceSimpleDTO.setId(task.getProcessInstanceId());
            processInstanceSimpleDTO.setKey(pd.getKey());
            processInstanceSimpleDTO.setName(pd.getName());
            processInstanceSimpleDTO.setVersion(pd.getVersion());
            processInstanceSimpleDTO.setTenantId(task.getTenantId());
            processInstanceSimpleDTO.setBusinessKey(task.getBusinessKey());
            processInstanceSimpleDTO.setStartUser(helper.getProcessStartUser(task.getProcessInstanceId()));
            // UserTaskDTO
            CurrentUserTaskDTO dto = new CurrentUserTaskDTO();
            dto.setId(task.getId());
            dto.setKey(task.getTaskDefinitionKey());
            dto.setName(task.getName());
            dto.setAssigneeUser(helper.getTaskAssigneeUser(task.getAssignee()));
            dto.setCandidateUsers(helper.getTaskCandidateUsers(task.getId()));
            dto.setClaimTime(DateUtils.parse(task.getClaimTime()));
            dto.setCreateTime(DateUtils.parse(task.getCreateTime()));
            dto.setFormKey(task.getFormKey());
            dto.setProcessInstance(processInstanceSimpleDTO);

            currentUserTasks.add(dto);
        });
        return currentUserTasks;
    }

    /**
     * 同意
     *
     * @param completeTaskCommand
     */
    @PostMapping("/process/task/complete")
    public void completeTask(@RequestBody @Valid CompleteTaskCommand completeTaskCommand) {
        if (StringUtils.isBlank(completeTaskCommand.getAccount())) {
            completeTaskCommand.setAccount(SecurityContextHolder.getPrincipal().getId().toString());
        }
        activatedTaskCommandService.complete(completeTaskCommand);
    }

    @PostMapping("/process/task/repeat")
    public void repeat(@RequestBody RepeatCommand repeatCommand) {
        if (StringUtils.isBlank(repeatCommand.getAccount())) {
            repeatCommand.setAccount(SecurityContextHolder.getPrincipal().getId().toString());
        }
        activatedTaskCommandService.repeat(repeatCommand);
    }

    /**
     * 转交
     *
     * @param transferCommand
     */
    @PostMapping("/process/task/transfer")
    public void transfer(@RequestBody @Valid TransferCommand transferCommand) {
        if (StringUtils.isBlank(transferCommand.getAccount())) {
            transferCommand.setAccount(SecurityContextHolder.getPrincipal().getId().toString());
        }
        activatedTaskCommandService.transfer(transferCommand);
    }

    @PostMapping("/process/task/add_multiple_sign")
    public void addMultipleSign(@RequestBody @Valid AddMultipleSignCommand addMultipleSignCommand) {
        activatedTaskCommandService.addMultipleSign(addMultipleSignCommand);
    }

    @PostMapping("/process/task/hasten")
    public void hasten(@RequestBody @Valid HastenCommand hastenCommand) {
        activatedTaskCommandService.hasten(hastenCommand);
    }

    /**
     * 驳回
     *
     * @param rejectCommand
     */
    @PostMapping("/process/task/reject")
    public void reject(@RequestBody @Valid RejectCommand rejectCommand) {
        if (StringUtils.isBlank(rejectCommand.getAccount())) {
            rejectCommand.setAccount(SecurityContextHolder.getPrincipal().getId().toString());
        }
        activatedTaskCommandService.reject(rejectCommand);
    }

    /**
     * 作废当前任务所在流程
     *
     * @param cancellationCurrentProcessInstanceDTO 任务信息
     */
    @PostMapping("/process/task/cancellation")
    public void cancellationCurrentProcessInstance(
            @RequestBody @Valid CancellationCurrentProcessInstanceDTO cancellationCurrentProcessInstanceDTO
                                                  ) {
        activatedTaskCommandService.cancellation(cancellationCurrentProcessInstanceDTO.getTaskId(),
                                                 SecurityContextHolder.getPrincipal().getId().toString(),
                                                 cancellationCurrentProcessInstanceDTO.getComment());
    }

    /**
     * 撤销
     *
     * @param undoCurrentProcessInstanceDTO
     */
    @PostMapping("/process/task/undo")
    public void undo(@RequestBody @Valid UndoCurrentProcessInstanceDTO undoCurrentProcessInstanceDTO) {
        activatedTaskCommandService.undo(undoCurrentProcessInstanceDTO.getProcessInstanceId(),
                                         SecurityContextHolder.getPrincipal().getId().toString(),
                                         undoCurrentProcessInstanceDTO.getComment());
    }

    /**
     * 退回
     *
     * @param backCommand
     */
    @PostMapping("/process/task/back")
    public void back(@RequestBody @Valid BackTaskCommand backCommand) {
        if (StringUtils.isBlank(backCommand.getAccount())) {
            backCommand.setAccount(SecurityContextHolder.getPrincipal().getId().toString());
        }
        if (!backCommand.isInnerServer()) {
            ProcessBackMessage processBackMessage = activatedTaskCommandService.back(backCommand);
            RocketMQContextHolder.send(ProcessBackMessage.TOPIC + ":" + processBackMessage.getBusinessType(), processBackMessage);
        }
    }

    /**
     * 撤回
     *
     * @param backCommand 撤回任务信息
     */
    @PostMapping("/process/task/withdraw")
    public void withdrawTask(@RequestBody @Valid BackTaskCommand backCommand) {
        ProcessWithdrawMessage message = activatedTaskCommandService.withdrawTask(backCommand);
        RocketMQContextHolder.send(ProcessWithdrawMessage.TOPIC + ":" + message.getBusinessType(), message);
    }

    @GetMapping("/process/task/form/id/{id}")
    public TaskInstanceDetailDTO getTaskFormById(@PathVariable("id") String processInstanceId) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        return processInstanceCacheService.getTaskFormById(processInstanceId, userId);
    }

    @GetMapping("/process/task/id/{id}")
    public TaskInstanceDetailDTO getTaskById(@PathVariable("id") String taskId) {
        Optional<TaskInstance> optional = taskInstanceRepository.get(taskId);
        if (optional.isPresent()) {
            TaskInstance taskInstance = optional.get();
            TaskInstanceDetailDTO taskInstanceDetailDTO = new TaskInstanceDetailDTO();
            taskInstanceDetailDTO.setId(taskId);
            taskInstanceDetailDTO.setClaimTime(DateUtils.parse(taskInstance.getTaskInfo().getClaimTime()));
            taskInstanceDetailDTO.setCreateTime(DateUtils.parse(taskInstance.getTaskInfo().getCreateTime()));
            taskInstanceDetailDTO.setKey(taskInstance.getTaskInfo().getTaskDefinitionKey());

            Optional<ExtProcessInstance> optionalExtProcessInstance = extProcessInstanceRepository.get(taskInstance.getTaskInfo()
                                                                                                               .getProcessInstanceId());
            if (!optionalExtProcessInstance.isPresent()) {
                throw new ServiceException("流程不存在");
            }
            taskInstanceDetailDTO.setData(optionalExtProcessInstance.get().getData());
            taskInstanceDetailDTO.setName(taskInstance.getTaskInfo().getName());

            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().includeProcessVariables()
                    .processInstanceId(taskInstance.getProcessInstanceId()).singleResult();
            ProcessDefinitionDTO processDefinitionDTO = processDefinitionQueryService.get(historicProcessInstance.getProcessDefinitionKey());
            Properties properties = new Properties();
            properties.put(VariableConstant.FORM_INSTANCE_Id, historicProcessInstance.getProcessVariables()
                    .get(VariableConstant.FORM_INSTANCE_Id));
            properties.put(ProcessConstant.PROCESS_JSON, processDefinitionDTO.getProcessFormJson());
            taskInstanceDetailDTO.setProperties(properties);
            return taskInstanceDetailDTO;
        }

        return null;
    }

    @GetMapping("/process/task/list/{processInstanceId}")
    public List<CurrentUserTaskDTO> getTasksByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        List<CurrentUserTaskDTO> currentUserTasks = Collections.synchronizedList(new ArrayList<>());
        tasks.parallelStream().forEach(task -> {
            // 流程实例
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            ProcessInstanceSimpleDTO processInstanceSimpleDTO = new ProcessInstanceSimpleDTO();
            processInstanceSimpleDTO.setId(task.getProcessInstanceId());
            processInstanceSimpleDTO.setKey(pd.getKey());
            processInstanceSimpleDTO.setName(pd.getName());
            processInstanceSimpleDTO.setVersion(pd.getVersion());
            processInstanceSimpleDTO.setTenantId(task.getTenantId());
            processInstanceSimpleDTO.setBusinessKey(task.getBusinessKey());
            processInstanceSimpleDTO.setStartUser(helper.getProcessStartUser(task.getProcessInstanceId()));
            // UserTaskDTO
            CurrentUserTaskDTO dto = new CurrentUserTaskDTO();
            dto.setId(task.getId());
            dto.setKey(task.getTaskDefinitionKey());
            dto.setName(task.getName());
            dto.setAssigneeUser(helper.getTaskAssigneeUser(task.getAssignee()));
            dto.setCandidateUsers(helper.getTaskCandidateUsers(task.getId()));
            dto.setClaimTime(DateUtils.parse(task.getClaimTime()));
            dto.setCreateTime(DateUtils.parse(task.getCreateTime()));
            dto.setFormKey(task.getFormKey());
            dto.setProcessInstance(processInstanceSimpleDTO);

            currentUserTasks.add(dto);
        });

        return currentUserTasks;
    }

    @GetMapping("/process/business/type/account/task/page")
    public Pagination<TaskInfoDTO> getTasksByBusinessType(TaskInfoWithBusinessTypeQuery query) {
        Set<String> processInstanceIds = processInstanceQueryService.getProcessInstanceIdByBusinessTypeAndPeriod(query.getBusinessType(), query.getSubmitTime());
        if (CollectionUtils.isEmpty(processInstanceIds)) {
            return new Pagination<>();
        }
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceIdIn(new ArrayList<>(processInstanceIds));

        if (StringUtils.isEmpty(query.getAccount())) {
            query.setAccount(SecurityContextHolder.getPrincipal().getId().toString());
            taskQuery.taskCandidateOrAssigned(query.getAccount());
        }

        List<Task> list = taskQuery.listPage(
                (query.getPageNumber() - 1) * query.getPageSize(), query.getPageSize()
                                            );

        long totalCount = taskQuery.count();

        List<TaskInfoDTO> taskInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {

            Map<String, HistoricProcessInstance> processInstanceMap = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceIds(processInstanceIds).includeProcessVariables().list()
                    .stream().collect(Collectors.toMap(HistoricProcessInstance::getId, v -> v));

            list.forEach(task -> {
                Object result = processInstanceMap.get(task.getProcessInstanceId()).getProcessVariables().get(VariableConstant.RESULT);
                taskInfos.add(new TaskInfoDTO(
                        task.getId(),
                        processInstanceMap.get(task.getProcessInstanceId()).getProcessDefinitionKey(),
                        task.getProcessInstanceId(),
                        processInstanceMap.get(task.getProcessInstanceId()).getProcessVariables().get(VariableConstant.REMARK).toString(),
                        DateUtils.parse(processInstanceMap.get(task.getProcessInstanceId()).getStartTime()),
                        Objects.isNull(result) ? "ACTIVE" : result.toString()
                ));
            });

            return Pagination.<TaskInfoDTO>builder().total(totalCount).pages((totalCount + query.getPageSize() - 1) / query.getPageSize())
                    .records(taskInfos).build();

        }

        return new Pagination<>();
    }

    /**
     * 指定审批人
     */
    @PostMapping("/process/task/appoint-approver")
    public void preAddMultipleSign(@Valid @RequestBody AppointApproverCommand appointApproverCommand) {
        activatedTaskCommandService.preAddMultipleSign(appointApproverCommand);
    }

    /**
     * 预减签
     *
     * @param
     */
    @PostMapping("/process/task/pre-reduce-sign")
    public void preReduceSign(@Valid @RequestBody PreReduceSignCommand preReduceSignCommand) {
        activatedTaskCommandService.preReduceSign(preReduceSignCommand);
    }

    /**
     * @param processInstanceId 流程实例id
     *
     * @return true 正处于第一个用户任务节点 false 不是第一个用户任务节点
     *
     * @description 当前流程处于的节点是否是第一个用户任务节点
     */
    @GetMapping("/flow/{processInstanceId}/first/task/active")
    public Boolean activated(@PathVariable("processInstanceId") String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (Objects.isNull(processInstance)) {
            return false;
        }
        List<Task> list = taskService.createTaskQuery().active().processInstanceId(processInstanceId).list();
        return TaskInstanceDomainService.isFirstArtificialTask(list.getFirst());
    }

    /**
     * 获取上一次用户签名图片
     *
     * @param
     */
    @PostMapping("/process/task/user-last-sign")
    public FileVO getAssignee() {
        Long id = SecurityContextHolder.getPrincipal().getId();
        if (Objects.isNull(id)) {
            return new FileVO();
        }
        return activatedTaskCommandService.getAssignee(id);
    }

    /**
     * 根据流程实例和审批人获取活跃任务
     */
    @GetMapping("/process/run/task/process-ins-id")
    public List<TaskVariableDTO> gerRunTaskByProcessInsIdAndAssignee(@RequestParam("processInstanceId") String processInstanceId,
                                                                     @RequestParam(value = "assignee", required = false) String assignee
                                                                    ) {
        return taskRuNodeMapper.queryRunTaskByProInsIdAndAssignee(processInstanceId, assignee);
    }

    /**
     * 根据当前工单获取来源工单id
     */
    @GetMapping("/process/source-id/get")
    public String getSourceProcessIdByProcessId(@RequestParam("processInstanceId") String processInstanceId) {
        return taskRuNodeMapper.getSourceProcessIdByProcessId(processInstanceId);
    }

    /**
     * 根据当前工单获取来源工单id
     */
    @PostMapping("/process/process-id/by/source")
    public Map<String, String> getProcessIdBySourceId(@RequestBody List<String> processInstanceIdList) {
        if (CollectionUtils.isEmpty(processInstanceIdList)) {
            return Collections.emptyMap();
        }
        List<ProcessIdBySourceDTO> processIdBySourceDTOS = taskRuNodeMapper.getProcessIdBySourceId(processInstanceIdList);
        if (CollectionUtils.isEmpty(processIdBySourceDTOS)) {
            return Collections.emptyMap();
        }
        return processIdBySourceDTOS.stream().collect(Collectors.toMap(ProcessIdBySourceDTO::getSourceId, ProcessIdBySourceDTO::getProcessId
                , (k1, k2) -> k2));
    }

}
