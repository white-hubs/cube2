package com.guhecloud.cube.base.flow.flowable.process.application.querygateway;

import base.constant.VariableConstant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.common.resovler.NodeUserParser;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.application.querygateway.ProcessDefinitionQueryService;
import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;
import com.guhecloud.cube.base.flow.flowable.definition.domain.repository.ProcessDefinitionRepository;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.OperateAlias;
import com.guhecloud.cube.base.flow.flowable.resolver.enumeration.AssigneeTypeEnums;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.domain.repository.TaskFeedbackLogRepository;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskRunningLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskLogPO;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskRunningPO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskFeedbackLogDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.flow.flowable.resolver.BpmnResolver.getChildNode;

/**
 * @author pengjian
 * @date 2022/9/1 22:04
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProcessInstanceQueryService {
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final ExtProcessInstanceMapper extProcessInstanceMapper;
    private final HistoryService historyService;
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final TaskService taskService;
    private final UserClient userClient;
    @Resource(name = "nodeUserParser")
    private NodeUserParser nodeUserParser;
    private final TaskRunningLogMapper taskRunningLogMapper;
    private final ProcessDefinitionQueryService processDefinitionQueryService;
    private final TaskFeedbackLogRepository taskFeedbackLogRepository;

    public Optional<ExtProcessInstance> getByIdOrBusinessKey(String instanceId, String businessKey) {

        if (StringUtils.isNotEmpty(instanceId)) {
            return extProcessInstanceRepository.get(instanceId);
        } else if (StringUtils.isNotEmpty(businessKey)) {
            return extProcessInstanceRepository.getByBusinessKey(businessKey);
        }

        throw new ServiceException("instanceId or bussinessKey 必须其一个有值");
    }

    public Set<String> getProcessInstanceIdByApplicationCodeAndBusinessType(String applicationCode, String businessType,
                                                                            Period submitTime
                                                                           ) {

        LambdaQueryWrapper<ExtProcessInstancePO> queryWrapper = Wrappers.<ExtProcessInstancePO>lambdaQuery()
                .eq(StringUtils.isNotEmpty(applicationCode), ExtProcessInstancePO::getApplicationCode, applicationCode)
                .eq(StringUtils.isNotEmpty(businessType), ExtProcessInstancePO::getBusinessType, businessType);

        if (Objects.nonNull(submitTime)) {
            queryWrapper.between(ExtProcessInstancePO::getCreateTime, submitTime.getBegin(), submitTime.getEnd());
        }

        List<ExtProcessInstancePO> extProcessInstances = extProcessInstanceMapper.selectList(
                queryWrapper
                                                                                            );

        if (CollectionUtils.isEmpty(extProcessInstances)) {
            return Collections.emptySet();
        }
        return extProcessInstances.stream().map(ExtProcessInstancePO::getId).collect(Collectors.toSet());
    }

    public Set<String> getProcessInstanceIdByBusinessTypeAndPeriod(String businessType, Period submitTime) {
        Assert.isNotAcceptable(StringUtils.isBlank(businessType), "业务类型不能为null");
        LambdaQueryWrapper<ExtProcessInstancePO> queryWrapper = Wrappers.<ExtProcessInstancePO>lambdaQuery()
                .eq(ExtProcessInstancePO::getBusinessType, businessType);
        if (Objects.nonNull(submitTime)) {
            queryWrapper.between(ExtProcessInstancePO::getCreateTime, submitTime.getBegin(), submitTime.getEnd());
        }
        List<ExtProcessInstancePO> extProcessInstances = extProcessInstanceMapper.selectList(
                queryWrapper
                                                                                            );
        if (CollectionUtils.isEmpty(extProcessInstances)) {
            return Collections.emptySet();
        }
        return extProcessInstances.stream().map(ExtProcessInstancePO::getId).collect(Collectors.toSet());
    }

    public Set<String> getProcessInstanceIdByBusinessType(String businessType, Set<String> proncessInstanceIds) {
        List<ExtProcessInstancePO> extProcessInstances = extProcessInstanceMapper.selectList(
                Wrappers.<ExtProcessInstancePO>lambdaQuery()
                        .eq(ExtProcessInstancePO::getBusinessType, businessType)
                        .in(ExtProcessInstancePO::getId, proncessInstanceIds)
                                                                                            );
        if (CollectionUtils.isEmpty(extProcessInstances)) {
            return Collections.emptySet();
        }
        return extProcessInstances.stream().map(ExtProcessInstancePO::getId).collect(Collectors.toSet());
    }

    public Long getFormInstanceIdByProcessInstanceId(String processInstanceId) {
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId)
                .variableName(VariableConstant.FORM_INSTANCE_Id).list();
        Long formInstanceId = null;
        if (CollectionUtils.isNotEmpty(list)) {
            formInstanceId = Long.valueOf(String.valueOf(list.get(0).getValue()));
        }

        return formInstanceId;
    }

    public boolean isSerial(String processInstanceId, String taskDefinitionKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        Process process = bpmnModel.getProcessById(processInstance.getProcessDefinitionKey());

        // 当前节点
        FlowElement flowElement = process.getFlowElement(taskDefinitionKey);

        UserTask userTask = (UserTask) flowElement;
        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
        return outgoingFlows.size() < 2;
    }

    public FlowElement getNextUserFlowElement(String processDefinitionId, String activityId) {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
        //获得当前流程的活动ID
        UserTask userTask = null;
        while (true) {
            //根据活动节点获取当前的组件信息
            FlowNode flowNode = getFlowNode(processDefinition.getId(), activityId);

            //获取该节点之后的流向
            List<SequenceFlow> sequenceFlowListOutGoing = flowNode.getOutgoingFlows();

            // 获取的下个节点不一定是userTask的任务节点，所以要判断是否是任务节点
            if (sequenceFlowListOutGoing.size() > 1) {

            } else if (sequenceFlowListOutGoing.size() == 1) {
                // 只有1条出线,直接取得下个节点
                SequenceFlow sequenceFlow = sequenceFlowListOutGoing.get(0);
                // 下个节点
                FlowElement flowElement = sequenceFlow.getTargetFlowElement();
                if (flowElement instanceof UserTask) {
                    // 下个节点为UserTask时
                    userTask = (UserTask) flowElement;
                    return userTask;
                } else if (flowElement instanceof ServiceTask) {
                    // 寻找下一个userTask
                    return nextUserTask(flowElement);
                }
                return null;
            } else {
                // 没有出线，则表明是结束节点
                return null;
            }
        }
    }

    /**
     * 获取当前任务节点的下一个任务节点
     *
     * @return 下个任务节点
     *
     * @throws Exception
     */
    public FlowElement getNextUserFlowElement(ProcessInstance processInstance, String activityId) {
        // 取得已提交的任务
        // 获得流程定义
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        //获得当前流程的活动ID
        UserTask userTask = null;
        while (true) {
            //根据活动节点获取当前的组件信息
            FlowNode flowNode = getFlowNode(processDefinition.getId(), activityId);

            //获取该节点之后的流向
            List<SequenceFlow> sequenceFlowListOutGoing = flowNode.getOutgoingFlows();

            // 获取的下个节点不一定是userTask的任务节点，所以要判断是否是任务节点
            if (sequenceFlowListOutGoing.size() > 1) {
                // 如果有1条以上的出线，表示有分支，需要判断分支的条件才能知道走哪个分支
                // 遍历节点的出线得到下个activityId
            } else if (sequenceFlowListOutGoing.size() == 1) {
                // 只有1条出线,直接取得下个节点
                SequenceFlow sequenceFlow = sequenceFlowListOutGoing.get(0);
                // 下个节点
                FlowElement flowElement = sequenceFlow.getTargetFlowElement();
                if (flowElement instanceof UserTask) {
                    // 下个节点为UserTask时
                    userTask = (UserTask) flowElement;
                    return userTask;
                } else if (flowElement instanceof ServiceTask) {
                    // 寻找下一个userTask
                    return nextUserTask(flowElement);
                }

                return null;

            } else {
                // 没有出线，则表明是结束节点
                return null;
            }
        }
    }

    private UserTask nextUserTask(FlowElement flowElement) {
        if (flowElement instanceof UserTask) {
            return (UserTask) flowElement;
        } else if (flowElement instanceof ServiceTask) {
            ServiceTask serviceTask = (ServiceTask) flowElement;
            List<SequenceFlow> outgoingFlows = serviceTask.getOutgoingFlows();
            SequenceFlow sequenceFlow = outgoingFlows.get(0);
            FlowElement next = sequenceFlow.getTargetFlowElement();
            return nextUserTask(next);
        }
        return null;
    }

    /**
     * 根据活动节点和流程定义ID获取该活动节点的组件信息
     */
    public FlowNode getFlowNode(String processDefinitionId, String flowElementId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(flowElementId);
        return flowNode;
    }

    /**
     * 根据key判断是否还有正在进行中的流程实例
     *
     * @param processDefinitionKey 流程定义key
     *
     * @return true: 流程正在进行中  false:没有正在进行中的流程
     */
    public boolean isRunning(@NotBlank String processDefinitionKey) {
        long count = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .count();
        return count > 0;
    }

    public List<TaskLog> getHistoricLinePath(String processInstanceId, String flowKey) {
        List<TaskLog> result = new ArrayList<>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(flowKey).singleResult();
        ExecutionEntityImpl execution = new ExecutionEntityImpl();
        execution.setId(ProcessConstant.PRE_SHOW);
        execution.setProcessInstanceId(ProcessConstant.PRE_SHOW);
        execution.setProcessDefinitionKey(flowKey);
        flowKey = processDefinition.getId();
        execution.setProcessDefinitionId(flowKey);
        // 如果processInstanceId为null表示读取最新流程路径显示
        if (Objects.isNull(processInstanceId)) {
            TaskLog root = buildRootTask();
            result.add(root);
            FlowElement flowElement = this.getNextUserFlowElement(flowKey, VariableConstant.ROOT);
            UserTask userTask = null;
            while (Objects.nonNull(flowElement)) {
                userTask = (UserTask) flowElement;
                TaskLog taskLog = buildNodeInfo(userTask, execution, VariableConstant.ROOT);
                setSelfSelectInfo(taskLog, userTask.getId(), execution);
                result.add(taskLog);
                flowElement = this.getNextUserFlowElement(flowKey, userTask.getId());
            }
            return result;
        }

        /**
         * 获取当前流程实例是否是串行流程
         * 如果是串行流程 显示历史操作+下个节点
         * 如果是分支操作，需要显示到分支的那个节点
         */
        /**
         * 如果此流程结束，则不用管
         */
        HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(processInstanceId).singleResult();
        if (Objects.nonNull(taskInstance)) {
            processInstanceId = taskInstance.getProcessInstanceId();
        }
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).includeProcessVariables().singleResult();

        // 构建历史
        TaskLogMapper taskLogMapper = ApplicationContextHolder.getBean(TaskLogMapper.class);
        List<TaskLogPO> taskLogPOS = taskLogMapper.selectList(
                Wrappers.<TaskLogPO>lambdaQuery()
                        .eq(TaskLogPO::getProcessInstanceId, historicProcessInstance.getId())
                        .orderByAsc(TaskLogPO::getCreateTime));

        if (CollectionUtils.isEmpty(taskLogPOS)) {
            return Collections.emptyList();
        }
        List<TaskFeedbackLogDTO> taskFeedbackLog = taskFeedbackLogRepository.getTaskFeedbackLog(historicProcessInstance.getId(), null);
        Map<String, List<TaskFeedbackLogDTO>> taskFeedbackMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(taskFeedbackLog)) {
            taskFeedbackMap = taskFeedbackLog.stream().collect(Collectors.groupingBy(TaskFeedbackLogDTO::getTaskNodeKey));
        }
        Map<String, List<TaskFeedbackLogDTO>> finalTaskFeedbackMap = taskFeedbackMap;
        LocalDateTime lastNodeCreateTime = taskLogPOS.get(0).getCreateTime();
        for (int i = 0; i < taskLogPOS.size(); i++) {
            TaskLogPO taskLogPO = taskLogPOS.get(i);
            // 剩余处理人处理
            // 获取当前任务剩余处理人
            TaskLog taskLog = JsonUtils.deserialize(taskLogPO.getOperationInfo(), TaskLog.class);
            if (finalTaskFeedbackMap.containsKey(taskLog.getNodeKey()) && CollectionUtils.isNotEmpty(taskLog.getTaskOperations())) {
                List<TaskFeedbackLogDTO> taskFeedbackLogDTOS = finalTaskFeedbackMap.get(taskLog.getNodeKey());
                for (TaskLog.TaskOperation taskOperation : taskLog.getTaskOperations()) {
                    if (Objects.nonNull(taskOperation.getOperation()) && Objects.nonNull(taskOperation.getOperation().getOperator())) {
                        Long userId = taskOperation.getOperation().getOperator().getId();
                        //操作人相同且反馈时间在操作时间之前
                        LocalDateTime finalLastNodeCreateTime = lastNodeCreateTime;
                        taskOperation.setFeedbackLogs(taskFeedbackLogDTOS.stream().filter(
                                        t -> userId.equals(t.getFeedbackUserId())
                                             && DateUtils.compare(t.getFeedbackTime(), finalLastNodeCreateTime) > 0
                                             && DateUtils.compare(t.getFeedbackTime(), taskOperation.getOperation().getCreateTime()) < 0)
                                                              .collect(Collectors.toList()));
                    }
                }
            }
            lastNodeCreateTime = taskLogPO.getCreateTime();
            result.add(taskLog);
        }

        TaskLog lastTaskLog = result.get(result.size() - 1);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(lastTaskLog.getNodeKey())
                .list();
        Set<Long> assignees = tasks.stream().map(task -> Long.parseLong(task.getAssignee())).collect(Collectors.toSet());
        List<TaskLog.Operator> operatorList = new ArrayList<>();
        Map<Long, String> userIdToNameMap = userClient.listUserByIds(assignees)
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        assignees.forEach(assignee -> operatorList.add(new TaskLog.Operator(assignee, userIdToNameMap.get(assignee), false)));
        operatorList.forEach(assignee -> lastTaskLog.getTaskOperations().add(
                TaskLog.TaskOperation.builder()
                        .residualOperator(assignee)
                        .build()));
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().includeProcessVariables()
                .processInstanceId(processInstanceId).singleResult();
        if (Objects.isNull(processInstance)) {
            return result;
        }
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().list();
        boolean serial = this.isSerial(processInstance.getId(), list.get(0).getTaskDefinitionKey());
        if (!serial) {
            return result;
        }
        String currentTaskKey = list.get(0).getTaskDefinitionKey();
        // 处理当前任务节点
        BpmnModel bpmnModel = repositoryService.getBpmnModel(list.get(0).getProcessDefinitionId());
        // 当前任务节点对象，封装了节点的元数据，如入线和出线
        UserTask activeTaskNode = (UserTask) bpmnModel.getFlowElement(list.get(0).getTaskDefinitionKey());

        // 当前活跃节点如果已经有了直接累加，如果没有就直接创建加入
        Optional<TaskLog> optional = result.stream().filter(taskLog -> taskLog.getNodeKey().equals(activeTaskNode.getId())).findFirst();
        if (optional.isPresent()) {
            List<Task> activatedAssignees = taskService.createTaskQuery().taskDefinitionKey(activeTaskNode.getId())
                    .processInstanceId(processInstanceId).list();
            List<TaskLog.Operator> operators = new ArrayList<>();
            Map<Long, String> userIdToNameMap2 = userClient
                    .listUserByIds(activatedAssignees.stream().map(activatedAssignee -> Long.parseLong(activatedAssignee.getAssignee()))
                                           .collect(Collectors.toSet()))
                    .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
            activatedAssignees.forEach(activatedAssignee -> {
                long assignee = Long.parseLong(activatedAssignee.getAssignee());
                operators.add(new TaskLog.Operator(assignee, userIdToNameMap2.get(assignee)));
            });
        } else {
            // 直接创建
            List<Task> activatedAssignees = taskService.createTaskQuery().taskDefinitionKey(activeTaskNode.getId())
                    .processInstanceId(processInstanceId).list();
            List<TaskLog.Operator> operators = new ArrayList<>();
            Map<Long, String> userIdToNameMap3 = userClient
                    .listUserByIds(activatedAssignees.stream().map(activatedAssignee -> Long.parseLong(activatedAssignee.getAssignee()))
                                           .collect(Collectors.toSet()))
                    .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
            activatedAssignees.forEach(activatedAssignee -> {
                long assignee = Long.parseLong(activatedAssignee.getAssignee());
                operators.add(new TaskLog.Operator(assignee, userIdToNameMap3.get(assignee)));
            });

            List<TaskLog.TaskOperation> taskOperations = new ArrayList<>();
            operators.forEach(operator -> taskOperations.add(
                    TaskLog.TaskOperation.builder()
                            .residualOperator(operator)
                            .build()));

            TaskLog taskLog = TaskLog.builder()
                    .nodeKey(activeTaskNode.getId())
                    .nodeName(activeTaskNode.getName())
                    .taskOperations(
                            taskOperations
                                   )
                    .preAddSign(false)
                    .build();
            result.add(taskLog);
        }

        TaskLog taskLog = buildNodeInfo(activeTaskNode, processInstance, currentTaskKey);

        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).taskDefinitionKey(activeTaskNode.getId()).finished().list();
        List<Task> runningTasks = taskService.createTaskQuery().processInstanceId(processInstanceId)
                .taskDefinitionKey(activeTaskNode.getId()).active().list();
        List<String> runningAssignees = runningTasks.stream().map(Task::getAssignee).collect(Collectors.toList());
        List<String> historyAssignees = new ArrayList<>();
        Map<Long, String> userIdToNameMap3 = userClient
                .listUserByIds(runningAssignees.stream().map(Long::parseLong)
                                       .collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        Map<Long, String> userIdToNameMap4 = userClient
                .listUserByIds(historyAssignees.stream().map(Long::parseLong)
                                       .collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        List<TaskLog.Operator> runningOperators = new ArrayList<>();
        List<TaskLog.Operator> historyOperators = new ArrayList<>();
        runningAssignees.forEach(assignee -> {
            runningOperators.add(new TaskLog.Operator(Long.parseLong(assignee), userIdToNameMap3.get(Long.parseLong(assignee))));
        });

        if (CollectionUtils.isNotEmpty(historicTaskInstances)) {
            historyAssignees = historicTaskInstances.stream().map(HistoricTaskInstance::getAssignee).collect(Collectors.toList());
            historyAssignees.forEach(assignee -> {
                historyOperators.add(new TaskLog.Operator(Long.parseLong(assignee), userIdToNameMap4.get(Long.parseLong(assignee))));
            });
        }
        taskLog.setAssigneeList(Collections.emptyList());

        while (true) {
            if (runningOperators.isEmpty()) {
                break;
            }
            if (CollectionUtils.isEmpty(taskLog.getTaskOperations())) {
                try {
                    taskLog.setTaskOperations(
                            Collections.singletonList(
                                    TaskLog.TaskOperation.builder()
                                            .residualOperator(runningOperators.get(0))
                                            .build())
                                             );

                } catch (Exception e) {

                } finally {
                    try {
                        runningOperators.remove(0);
                    } catch (Exception e) {

                    }
                }
            } else {
                try {
                    List<TaskLog.TaskOperation> taskOperations = new ArrayList<>();
                    taskOperations.addAll(taskLog.getTaskOperations());
                    taskOperations.add(TaskLog.TaskOperation.builder()
                                               .residualOperator(runningOperators.get(0))
                                               .build());
                    taskLog.setTaskOperations(taskOperations);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        runningOperators.remove(0);
                    } catch (Exception e) {

                    }
                }

            }

        }

        TaskLog last = result.get(result.size() - 1);
        if (!last.getNodeKey().equals(taskLog.getNodeKey())) {
            result.add(taskLog);
        }

        FlowElement flowElement = this.getNextUserFlowElement(processInstance, currentTaskKey);
        UserTask userTask = null;
        while (Objects.nonNull(flowElement)) {
            userTask = (UserTask) flowElement;
            TaskLog historicTaskInfoAndPreviewDTO = buildNodeInfo(userTask, processInstance, currentTaskKey);
            result.add(historicTaskInfoAndPreviewDTO);

            flowElement = this.getNextUserFlowElement(processInstance, userTask.getId());
        }
        for (TaskLog t : result) {
            if (!finalTaskFeedbackMap.containsKey(t.getNodeKey()) || CollectionUtils.isEmpty(t.getTaskOperations())) {
                continue;
            }
            List<TaskFeedbackLogDTO> taskFeedbackLogDTOS = finalTaskFeedbackMap.get(taskLog.getNodeKey());
            LocalDateTime checkTime = null;
            for (TaskLog.TaskOperation taskOperation : t.getTaskOperations()) {
                if (Objects.nonNull(taskOperation.getOperation()) && Objects.nonNull(taskOperation.getOperation().getOperator())) {
                    checkTime = taskOperation.getOperation().getCreateTime();
                    continue;
                }
                if (Objects.nonNull(taskOperation.getResidualOperator())) {
                    Long userId = taskOperation.getResidualOperator().getId();
                    List<TaskFeedbackLogDTO> collect = taskFeedbackLogDTOS.stream().filter(
                                    feedback -> userId.equals(feedback.getFeedbackUserId()))
                            .collect(Collectors.toList());
                    if (Objects.nonNull(checkTime) && CollectionUtils.isNotEmpty(collect)) {
                        LocalDateTime finalCheckTime = checkTime;
                        collect = collect.stream().filter(feedback -> DateUtils.compare(feedback.getFeedbackTime(), finalCheckTime) > 0)
                                .collect(Collectors.toList());
                    }
                    taskOperation.setFeedbackLogs(collect);

                }
            }
        }
        return result;
    }

    public TaskLog buildNodeInfo(UserTask userTask, ProcessInstance processInstance, String currentTaskKey) {
        Boolean preAddSign = getPreAddSign(processInstance.getProcessDefinitionId(), processInstance.getProcessInstanceId(), processInstance.getStartUserId());
        Long startUserId = Objects.nonNull(processInstance.getStartUserId()) ? Long.parseLong(processInstance.getStartUserId())
                                                                             : SecurityContextHolder.getPrincipal().getId();
        List<Long> userIds = nodeUserParser.parse(userTask.getId(), (ExecutionEntity) processInstance, startUserId);
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<UserDTO> userDTOS = userClient.listUserByIds(userIds);
            BusinessKeysParam businessKeysParam = new BusinessKeysParam();
            businessKeysParam.setBusinessKeys(userDTOS.stream().map(UserDTO::getId).collect(Collectors.toList()));
            List<TaskLog.Operator> operators = new ArrayList<>();
            userDTOS.forEach(userDTO -> {
                // 是否可以减签标识; 此人员是否包含在流程实例中的临时人员
                String variable = userTask.getId() + VariableConstant.ASSIGNEE_List;
                Boolean preReduceSign = false;
                Set<String> users = new HashSet<>();
                if (!processInstance.getId().equals(ProcessConstant.PRE_SHOW)) {
                    Object addUsers = runtimeService.getVariable(processInstance.getId(), VariableConstant.TEMPORARY + variable);
                    if (ObjectUtils.isNotEmpty(addUsers)) {
                        users.addAll((List<String>) addUsers);
                    }
                }
                // 如果是当前节点就直接为false
                if (!userTask.getId().equals(currentTaskKey)) {
                    Long userId = 0L;
                    try {
                        userId = SecurityContextHolder.getPrincipal().getId();
                    } catch (Exception e) {
                        log.info("外部用户调用");
                    }
                    // 获取当前节点剩下未处理的人员
                    List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId())
                            .taskDefinitionKey(currentTaskKey).active().list();
                    Set<Long> assignees = tasks.stream().map(task -> Long.parseLong(task.getAssignee())).collect(Collectors.toSet());
                    List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                            .processInstanceId(processInstance.getProcessInstanceId()).finished().list();
                    Map<String, HistoricTaskInstance> existMap = historicTaskInstances.stream()
                            .filter(distinctByKey(d -> d.getTaskDefinitionKey()))
                            .collect(Collectors.toMap(HistoricTaskInstance::getTaskDefinitionKey, h -> h));
                    // 如果当前用户在里面并且开启了配置， 那么展示减签标识(已经完成了的节点不用展示)
                    ExtProcessDefinition processDefinition = ApplicationContextHolder.getBean(ProcessDefinitionRepository.class)
                            .getById(processInstance.getProcessDefinitionId());
                    ChildNode currentNode = processDefinition.getCurrentNode(currentTaskKey);
                    Boolean designatedApprover =
                            Objects.nonNull(currentNode.getProps().getDesignatedApprover()) ? currentNode.getProps().getDesignatedApprover()
                                                                                            : false;
                    if (assignees.contains(userId) && !existMap.containsKey(userTask.getId()) && designatedApprover &&
                        CollectionUtils.isNotEmpty(users) && users.contains(userDTO.getId().toString())) {
                        preReduceSign = true;
                    } else {
                        preReduceSign = false;
                    }
                }
                TaskLog.Operator operator = new TaskLog.Operator(userDTO.getId(), userDTO.getName(), preReduceSign);
                operators.add(operator);
            });

            return TaskLog.builder()
                    .nodeKey(userTask.getId())
                    .nodeName(userTask.getName())
                    .assigneeList(operators)
                    .preAddSign(preAddSign)
                    .build();

        }
        return null;

    }

    private TaskLog buildRootTask() {
        UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        return TaskLog.builder()
                .nodeKey(VariableConstant.ROOT)
                .nodeName("发起人")
                .taskOperations(
                        Collections.singletonList(
                                TaskLog.TaskOperation.builder()
                                        .operation(new TaskLog.Operation(
                                                TaskOperationType.SUBMIT.name(),
                                                "发起",
                                                null,
                                                new TaskLog.Operator(
                                                        SecurityContextHolder.getPrincipal().getId(),
                                                        user.getName()
                                                ),
                                                LocalDateTime.now(),
                                                null, null
                                        ))
                                        .build()
                                                 )
                               )
                .build();
    }

    /**
     * @Author wql
     * @Description 发起人自选判断
     * @Date 19:56 2023/11/29
     **/
    private void setSelfSelectInfo(TaskLog taskLog, String taskNodeId, ProcessInstance processInstance) {
        Process mainProcess = ApplicationContextHolder.getBean(RepositoryService.class)
                .getBpmnModel(processInstance.getProcessDefinitionId()).getMainProcess();
        String processJson = mainProcess.getAttributeValue(ProcessConstant.NAMESPACE, ProcessConstant.PROCESS_FORM_JSON);
        ChildNode childNode = JsonUtils.deserialize(processJson, new TypeReference<ChildNode>() {});
        while (ObjectUtils.isNotEmpty(childNode)) {
            if (taskNodeId.equals(childNode.getId())) {
                Map<String, Object> selfSelect = childNode.getProps().getSelfSelect();
                String assignedType = childNode.getProps().getAssignedType();
                if (AssigneeTypeEnums.SELF_SELECT.name().equals(assignedType) && ObjectUtils.isNotEmpty(selfSelect)) {
                    taskLog.setSelfSelect(true);
                    TaskLog.SelfSelectRange selfSelectRange = new TaskLog.SelfSelectRange();
                    List<String> ids = new ArrayList<>();
                    String type = String.valueOf(selfSelect.get("type"));
                    selfSelectRange.setType(type);
                    Map<String, Object> map = (Map<String, Object>) selfSelect.get("typeForSelectRange");
                    if (ObjectUtils.isNotEmpty(map)) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(type);
                        if (ObjectUtils.isNotEmpty(list)) {
                            list.forEach(info -> {
                                ids.add(String.valueOf(info.get("id")));
                            });
                        }
                        selfSelectRange.setIds(ids);
                        taskLog.setSelectRange(selfSelectRange);
                    }
                }
                break;
            } else if (ObjectUtils.isNotEmpty(childNode.getChildren())) {
                childNode = childNode.getChildren();
            }
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private Boolean getPreAddSign(String definitionId, String processInstanceId, String startUserId) {
        Process mainProcess = ApplicationContextHolder.getBean(RepositoryService.class).getBpmnModel(definitionId).getMainProcess();
        String processJson = mainProcess.getAttributeValue(ProcessConstant.NAMESPACE, ProcessConstant.PROCESS_FORM_JSON);
        ChildNode childNode = JsonUtils.deserialize(processJson, new TypeReference<ChildNode>() {});
        List<Task> list = taskService.createTaskQuery().active().processInstanceId(processInstanceId).list();
        String originalTaskKey = "";
        if (CollectionUtils.isNotEmpty(list)) {
            Task task = list.get(0);
            originalTaskKey = task.getTaskDefinitionKey();
        }
        if (StringUtils.isEmpty(originalTaskKey)) {
            return false;
        }
        ChildNode currentNode = getChildNode(childNode, originalTaskKey);
        Boolean designatedApprover = currentNode.getProps().getDesignatedApprover();
        if (Objects.isNull(designatedApprover) || Boolean.TRUE.equals(!designatedApprover)) {
            return false;
        }

        // 只有当前节点的人员才能加签
        List<HistoricTaskInstance> historicTaskInstances = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .unfinished()
                .taskDefinitionKey(originalTaskKey)
                .list();
        Set<Long> available = historicTaskInstances.stream().map(historicTaskInstance -> Long.parseLong(historicTaskInstance.getAssignee()))
                .collect(Collectors.toSet());
        Long userId = StringUtils.isNotEmpty(startUserId) ? Long.parseLong(startUserId) : 0L;
        try {
            userId = SecurityContextHolder.getPrincipal().getId();
        } catch (Exception e) {
            log.error("外部用户调用");
        }

        if (!available.contains(userId)) {
            return false;
        }

        return true;

    }

    public List<TaskLog> getFlowPrintNodeInfo(String processInstanceId) {
        //兼容方案
        HistoricTaskInstance instance = historyService.createHistoricTaskInstanceQuery().taskId(processInstanceId).singleResult();
        if (Objects.nonNull(instance)) {
            processInstanceId = instance.getProcessInstanceId();
        }
        List<TaskRunningPO> runningPOList = taskRunningLogMapper.selectList(
                Wrappers.<TaskRunningPO>lambdaQuery()
                        .eq(TaskRunningPO::getProcessInstanceId, processInstanceId)
                        .orderByAsc(TaskRunningPO::getCreateTime));
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        ProcessDefinitionDTO processDefinitionDTO = processDefinitionQueryService.get(historicProcessInstance.getProcessDefinitionKey());

        return runningPOList.stream().map(po -> {
                    TaskLog taskLog = JsonUtils.deserialize(po.getOperationInfo().replace("\\", ""), TaskLog.class);
                    setOperationAlias(po, processDefinitionDTO, taskLog);
                    return taskLog;
                })
                //发起人节点和抄送节点不需要打印
                .filter(taskLog -> !"root".equals(taskLog.getNodeKey()) && ObjectUtils.isNotEmpty(taskLog.getTaskOperations()) &&
                                   !TaskOperationType.COPY.name().equals(taskLog.getTaskOperations().get(0).getOperation().getType()))
                .collect(Collectors.toList());
    }

    /**
     * 设置操作别名
     *
     * @param po
     * @param processDefinitionDTO
     * @param taskLog
     */
    private void setOperationAlias(TaskRunningPO po, ProcessDefinitionDTO processDefinitionDTO, TaskLog taskLog) {
        ChildNode childNode = ExtProcessDefinition.getChildNode(JsonUtils.deserialize(processDefinitionDTO.getProcessFormJson(), new TypeReference<ChildNode>() {}),
                                                                po.getTaskKey());
        if (childNode != null && childNode.getProps() != null && childNode.getProps().getOperateAlias() != null) {
            for (TaskLog.TaskOperation taskOperation : taskLog.getTaskOperations()) {
                Optional<OperateAlias> operateAlias = childNode.getProps().getOperateAlias()
                        .stream().filter(t -> StringUtils.equals(t.getCode(), taskOperation.getOperation().getType())).findFirst();
                operateAlias.ifPresent(alias -> {
                    taskOperation.getOperation().setTypeAlias(alias.getAlias());
                    taskOperation.getOperation().setPerm(alias.getPerm());
                });
            }
        }
    }
}
