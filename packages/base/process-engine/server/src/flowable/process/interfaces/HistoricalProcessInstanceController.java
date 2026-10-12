package com.guhecloud.cube.base.flow.flowable.process.interfaces;

import base.constant.VariableConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.flowable.common.util.DateConvert;
import com.guhecloud.cube.base.flow.flowable.definition.application.querygateway.ProcessDefinitionQueryService;
import com.guhecloud.cube.base.flow.flowable.process.application.dto.ActiveNodeDTO;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.ProcessInstanceQueryService;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.AllFlowOrderPageQuery;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.ProcessInstanceHistoricalQuery;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.ProcessInstancePageQuery;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ProcessStatus;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.PageOfAllDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.PageOfDataBoardDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.PageOfHandledDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.PageOfServedMeDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.PageOfSubmitedDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.PageOfWaitingDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.PageOfWaitingTasksDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.TaskLogVO;
import com.guhecloud.cube.base.flow.flowable.task.application.dto.CopyLogDTO;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.application.querygateway.TaskInstanceQueryService;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.CopyLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskRunningLogMapper;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.HistoricVariableInstanceQueryProperty;
import org.activiti.engine.impl.ProcessInstanceQueryProperty;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/10/25 16:01
 * @description
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HistoricalProcessInstanceController {

    private final TaskService taskService;
    private final ProcessInstanceQueryService processInstanceQueryService;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final TaskInstanceQueryService taskInstanceQueryService;
    private final CopyLogMapper copyLogMapper;
    private final UserClient userClient;
    private final RepositoryService repositoryService;

    private final ExtProcessInstanceMapper extProcessInstanceMapper;
    private final TaskRunningLogMapper taskRunningLogMapper;
    private final ProcessDefinitionQueryService processDefinitionQueryService;

    /**
     * @Author wql
     * @Description 个人流程实例列表
     * @Date 17:25 2023/5/23
     **/
    @GetMapping("/process/instance/historical/page")
    public Pagination<ProcessInstanceDTO> page(@Valid ProcessInstancePageQuery processInstancePageQuery) {
        return taskInstanceQueryService.page(processInstancePageQuery);
    }

    /**
     * 个人流程实例列表导出（查询条件与 {@link #page} 一致）
     */
    @GetMapping("/process/instance/historical/page/export")
    public void processInstancePersonalHistoricalExport(@Valid ProcessInstancePageQuery processInstancePageQuery,
                                                        @RequestParam(required = false) String exportName,
                                                        HttpServletResponse response) {
        taskInstanceQueryService.processInstancePersonalHistoricalExport(processInstancePageQuery, exportName, response);
    }

    /**
     * @Author wql
     * @Description 所有流程实例列表
     * @Date 11:55 2023/7/12
     **/
    @GetMapping("/process/instance/historical/allPage")
    public Pagination<ProcessInstanceDTO> allPage(@Valid ProcessInstancePageQuery processInstancePageQuery) {
        return taskInstanceQueryService.allPage(processInstancePageQuery);
    }

    /**
     * 批量查询流程表单
     *
     * @param processInstancePageQuery
     *
     * @return
     */
    @PostMapping("/process/instance/historical/batch")
    Pagination<ProcessInstanceDTO> allPageBatch(@Valid @RequestBody ProcessInstancePageQuery processInstancePageQuery) {
        return taskInstanceQueryService.allPageBatch(processInstancePageQuery);
    }

    /**
     * 根据流程id查询流程实例
     */
    @GetMapping("/process/instance/historical/get/id")
    public ProcessInstanceDTO processInstanceGet(String processInsId) {
        return taskInstanceQueryService.processInstanceGet(processInsId);

    }

    /**
     * 所有工单列表分页查询
     *
     * @param allFlowOrderPageQuery 查询条件
     *
     * @return 工单列表
     */
    @GetMapping("/process/instance/flowOrder/all")
    public Pagination<ProcessInstanceDTO> flowOrderAll(@Valid AllFlowOrderPageQuery allFlowOrderPageQuery) {
        return taskInstanceQueryService.flowOrderAll(allFlowOrderPageQuery);
    }

    /**
     * @Author wql
     * @Description 数据看板流程实例
     * @Date 9:48 2023/11/8
     **/
    @GetMapping("/process/instance/historical/page/backlog")
    public Pagination<ProcessInstanceDTO> pageOfDataBoard(PageOfDataBoardDTO query) {
        ProcessInstancePageQuery instancePageQuery = new ProcessInstancePageQuery();
        instancePageQuery.setSubmitTime(query.getPeriod());
        instancePageQuery.setStatus(query.getStatus());
        instancePageQuery.setApplicationCode(query.getApplicationCode());
        instancePageQuery.setBusinessType(query.getBusinessType());
        instancePageQuery.setPageSize(query.getPageSize());
        instancePageQuery.setPageNumber(query.getPageNumber());
        instancePageQuery.setType("HANDLED".equals(query.getType()) ? "PROCESSED" : query.getType());
        return page(instancePageQuery);
    }

    /**
     * @Author wql
     * @Description 待办列表
     * @Date 14:42 2023/6/29
     **/
    @GetMapping("/process/instance/historical/page/waiting-tasks")
    @Deprecated
    public Pagination<ProcessInstanceDTO> pageOfWaitingTasks(PageOfWaitingTasksDTO query) {
        ProcessInstancePageQuery pageQuery = new ProcessInstancePageQuery();
        pageQuery.setSubmitTime(query.getTaskCreateTime());
        pageQuery.setBusinessType(query.getBusinessType());
        pageQuery.setApplicationCode(query.getApplicationCode());
        pageQuery.setPageNumber(query.getPageNumber());
        pageQuery.setPageSize(query.getPageSize());
        pageQuery.setStatus(query.getStatus());
        return taskInstanceQueryService.waitingPage(pageQuery, SessionContextHolder.getRequester().getTenant());
    }

    /**
     * @Author wql
     * @Description 已处理列表
     * @Date 10:26 2023/6/29
     **/
    @GetMapping("/process/instance/historical/page/handled")
    @Deprecated
    public Pagination<ProcessInstanceDTO> pageOfHandle(PageOfHandledDTO query) {
        ProcessInstancePageQuery pageQuery = new ProcessInstancePageQuery();
        pageQuery.setSubmitTime(query.getTaskCompletedTime());
        pageQuery.setBusinessType(query.getBusinessType());
        pageQuery.setApplicationCode(query.getApplicationCode());
        pageQuery.setPageNumber(query.getPageNumber());
        pageQuery.setPageSize(query.getPageSize());
        pageQuery.setStatus(query.getStatus());
        pageQuery.setStartUserId(SecurityContextHolder.getPrincipal().getId().toString());
        return taskInstanceQueryService.handlePage(pageQuery, SessionContextHolder.getRequester().getTenant());
    }

    @GetMapping("/process/instance/historical/page/waiting")
    public Pagination<ProcessInstanceDTO> pageOfWaiting(@Valid PageOfWaitingDTO query) {
        Set<String> processInstanceIds = processInstanceQueryService.getProcessInstanceIdByBusinessTypeAndPeriod(query.getBusinessType(), null);
        if (CollectionUtils.isEmpty(processInstanceIds)) {
            return Pagination.empty();
        }

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery().active()
                .processInstanceIds(processInstanceIds);

        if (Objects.nonNull(query.getPeriod())) {
            processInstanceQuery.startedAfter(DateConvert.convertToDate(query.getPeriod().getBegin()))
                    .startedBefore(DateConvert.convertToDate(query.getPeriod().getEnd()));
        }
        List<ProcessInstance> processInstances = processInstanceQuery.list();

        if (CollectionUtils.isEmpty(processInstances)) {
            return Pagination.empty();
        }
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(SecurityContextHolder.getPrincipal().getId().toString())
                .processInstanceIdIn(processInstances.stream().map(ProcessInstance::getId).collect(Collectors.toList())).list();

        if (CollectionUtils.isEmpty(tasks)) {
            return Pagination.empty();
        }

        Set<String> processInstanceIdsOfWaiting = tasks.stream().map(Task::getProcessInstanceId).collect(Collectors.toSet());
        // 获取所有流程中的活跃节点名称
        Map<String, String> activeNodeNameMap = tasks.stream()
                .collect(Collectors.toMap(Task::getProcessInstanceId, task -> task.getName(), (key1, key2) -> key2));

        List<ProcessInstanceDTO> result = new ArrayList<>();

        long totalCount = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIdsOfWaiting).count();
        List<ProcessInstance> processInstancesList = runtimeService.createProcessInstanceQuery()
                .orderBy(new ProcessInstanceQueryProperty("RES.START_TIME_")).desc()
                .includeProcessVariables()
                .processInstanceIds(processInstanceIdsOfWaiting).listPage(
                        (query.getPageNumber() - 1) * query.getPageSize(), query.getPageSize()
                                                                         );

        Map<Long, String> userIdNameMap = userClient.listUserByIds(processInstancesList.stream()
                                                                           .map(processInstance -> Long.valueOf(processInstance.getStartUserId()))
                                                                           .collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        Map<String, List<Task>> instanceToTasksMap = taskService.createTaskQuery()
                .taskCandidateOrAssigned(SecurityContextHolder.getPrincipal().getId().toString()).active().processInstanceIdIn(
                        processInstancesList.stream().map(ProcessInstance::getId).collect(Collectors.toList())
                                                                                                                              ).list()
                .stream().collect(Collectors.groupingBy(Task::getProcessInstanceId));

        Map<String, Object> dataMap = historyService.createHistoricVariableInstanceQuery()
                .executionIds(processInstancesList.stream().map(ProcessInstance::getProcessInstanceId).collect(Collectors.toSet()))
                .variableName(VariableConstant.DATA)
                .list().stream()
                .collect(Collectors.toMap(HistoricVariableInstance::getProcessInstanceId, HistoricVariableInstance::getValue));

        Map<String, ExtProcessInstancePO> startUserInfoMap = extProcessInstanceMapper.selectList(
                        Wrappers.<ExtProcessInstancePO>lambdaQuery()
                                .in(ExtProcessInstancePO::getId, processInstancesList.stream().map(ProcessInstance::getProcessInstanceId)
                                        .collect(Collectors.toSet()))
                                                                                                ).stream().filter(Objects::nonNull)
                .collect(Collectors.toMap(ExtProcessInstancePO::getId, e -> e));

        // 查询流程变量
        processInstancesList.forEach(processInstance -> {
            String currentTaskId = null;
            List<Task> taskList = instanceToTasksMap.get(processInstance.getId());
            if (CollectionUtils.isNotEmpty(taskList)) {
                currentTaskId = taskList.get(0).getId();
            }
            Map<String, Object> variables = new HashMap<>();
            variables.put(VariableConstant.getVariableName(VariableConstant.DATA), dataMap.get(processInstance.getProcessInstanceId()));
            variables.put(VariableConstant.getVariableName(VariableConstant.FORM_NO), processInstance.getProcessVariables()
                    .get(VariableConstant.FORM_NO));
            variables.put(VariableConstant.getVariableName(VariableConstant.FORM_INSTANCE_Id), processInstanceQueryService.getFormInstanceIdByProcessInstanceId(processInstance.getProcessInstanceId()));
            ExtProcessInstancePO extProcessInstancePO = startUserInfoMap.getOrDefault(processInstance.getProcessInstanceId(), new ExtProcessInstancePO());
            result.add(ProcessInstanceDTO.builder()
                               .currentTaskId(currentTaskId)
                               .startUserInfo(extProcessInstancePO.getStartUserInfo())
                               .instanceName(extProcessInstancePO.getInstanceName())
                               .formNo(processInstance.getProcessVariables().get(VariableConstant.FORM_NO))
                               .processInstanceId(processInstance.getProcessInstanceId())
                               .variables(processInstance.getProcessVariables())
                               .submitedTime(DateUtils.parse(processInstance.getStartTime()))
                               .status(ProcessStatus.ACTIVE.name())
                               .formInstanceId(processInstanceQueryService.getFormInstanceIdByProcessInstanceId(processInstance.getProcessInstanceId()))
                               .startUserId(Long.valueOf(processInstance.getStartUserId()))
                               .startUserName(userIdNameMap.get(Long.valueOf(processInstance.getStartUserId())))
                               .activeNodeName(activeNodeNameMap.get(processInstance.getId()))
                               .variables(variables)
                               .build());

        });

        // 构建分页参数
        return Pagination.<ProcessInstanceDTO>builder().total(totalCount)
                .pages((totalCount + query.getPageSize() - 1) / query.getPageSize()).records(result).build();
    }

    /**
     * @Author wql
     * @Description 我发起的
     * @Date 14:39 2023/6/29
     **/
    @GetMapping("/process/instance/historical/page/submited")
    @Deprecated
    public Pagination<ProcessInstanceDTO> pageOfSubmited(@Valid PageOfSubmitedDTO query) {
        ProcessInstancePageQuery pageQuery = new ProcessInstancePageQuery();
        pageQuery.setSubmitTime(query.getPeriod());
        pageQuery.setBusinessType(query.getBusinessType());
        pageQuery.setApplicationCode(query.getApplicationCode());
        pageQuery.setPageNumber(query.getPageNumber());
        pageQuery.setPageSize(query.getPageSize());
        pageQuery.setStatus(query.getStatus());
        return taskInstanceQueryService.submitedPage(pageQuery, SessionContextHolder.getRequester().getTenant());
    }

    @GetMapping("/process/instance/historical/page/served_me")
    public Pagination<ProcessInstanceDTO> pageOfServedMe(@Valid PageOfServedMeDTO query) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        List<CopyLogDTO> copyLogs = copyLogMapper.selectListByUserId(userId);

        if (CollectionUtils.isEmpty(copyLogs)) {
            return new Pagination<>();
        }

        Set<String> processInstanceIds = processInstanceQueryService.getProcessInstanceIdByBusinessType(query.getBusinessType(),
                                                                                                        copyLogs.stream()
                                                                                                                .map(CopyLogDTO::getProcessInstanceId)
                                                                                                                .collect(Collectors.toSet()));
        if (CollectionUtils.isEmpty(processInstanceIds)) {
            return Pagination.empty();
        }

        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .orderByProcessInstanceStartTime().desc();
        if (Objects.nonNull(query.getSubmitPeriod())) {
            historicProcessInstanceQuery.startedAfter(DateConvert.convertToDate(query.getSubmitPeriod().getBegin()))
                    .startedBefore(DateConvert.convertToDate(query.getSubmitPeriod().getEnd()));
        }

        if (Objects.nonNull(query.getCompletePeriod())) {
            historicProcessInstanceQuery.finishedAfter(DateConvert.convertToDate(query.getCompletePeriod().getBegin()))
                    .finishedBefore(DateConvert.convertToDate(query.getCompletePeriod().getEnd()));
        }

        if (StringUtils.isNotEmpty(query.getStatus())) {
            if (query.getStatus().equals("ACTIVE")) {
                historicProcessInstanceQuery
                        .unfinished();
            } else if (query.getStatus().equals("FINISHED")) {
                List<ExtProcessInstancePO> extProcessInstancePOS = extProcessInstanceMapper.selectList(
                        Wrappers.<ExtProcessInstancePO>lambdaQuery()
                                .eq(ExtProcessInstancePO::getResult, TaskOperationType.PASS.name())
                                .in(ExtProcessInstancePO::getId, processInstanceIds)
                                                                                                      );
                if (CollectionUtils.isEmpty(extProcessInstancePOS)) {
                    return Pagination.empty();
                } else {
                    processInstanceIds = extProcessInstancePOS.stream().map(ExtProcessInstancePO::getId).collect(Collectors.toSet());
                    historicProcessInstanceQuery
                            .finished();
                }
            } else {
                List<ExtProcessInstancePO> extProcessInstancePOS = extProcessInstanceMapper.selectList(
                        Wrappers.<ExtProcessInstancePO>lambdaQuery()
                                .eq(ExtProcessInstancePO::getResult, query.getStatus())
                                .in(ExtProcessInstancePO::getId, processInstanceIds)
                                                                                                      );
                if (CollectionUtils.isEmpty(extProcessInstancePOS)) {
                    return Pagination.empty();
                } else {
                    processInstanceIds = extProcessInstancePOS.stream().map(ExtProcessInstancePO::getId).collect(Collectors.toSet());
                    historicProcessInstanceQuery
                            .finished();
                }
            }
        }

        historicProcessInstanceQuery.processInstanceIds(processInstanceIds);
        long totalCount = historicProcessInstanceQuery.count();

        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.listPage(
                (query.getPageNumber() - 1) * query.getPageSize(), query.getPageSize()
                                                                                                      );
        if (CollectionUtils.isEmpty(historicProcessInstances)) {
            return Pagination.empty();
        }

        Map<String, ActiveNodeDTO> activeNodeDTOMap = taskInstanceQueryService.listActiveNode(
                        historicProcessInstances.stream().map(HistoricProcessInstance::getId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(ActiveNodeDTO::getProcessInstanceId, value -> value));

        Map<Long, String> userIdNameMap = userClient.listUserByIds(historicProcessInstances.stream()
                                                                           .map(processInstance -> Long.valueOf(processInstance.getStartUserId()))
                                                                           .collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        Map<String, List<Task>> instanceToTasksMap = taskService.createTaskQuery()
                .taskCandidateOrAssigned(SecurityContextHolder.getPrincipal().getId().toString()).active().processInstanceIdIn(
                        historicProcessInstances.stream().map(HistoricProcessInstance::getId).collect(Collectors.toList())
                                                                                                                              ).list()
                .stream().collect(Collectors.groupingBy(Task::getProcessInstanceId));
        Map<String, Object> dataMap = historyService.createHistoricVariableInstanceQuery()
                .executionIds(historicProcessInstances.stream().map(HistoricProcessInstance::getId).collect(Collectors.toSet()))
                .variableName(VariableConstant.DATA)
                .list().stream()
                .collect(Collectors.toMap(HistoricVariableInstance::getProcessInstanceId, HistoricVariableInstance::getValue));

        List<ProcessInstanceDTO> result = new ArrayList<>();

        Map<String, ExtProcessInstancePO> startUserInfoMap = extProcessInstanceMapper.selectList(
                        Wrappers.<ExtProcessInstancePO>lambdaQuery()
                                .in(ExtProcessInstancePO::getId, historicProcessInstances.stream().map(HistoricProcessInstance::getId)
                                        .collect(Collectors.toSet()))
                                                                                                ).stream().filter(Objects::nonNull)
                .collect(Collectors.toMap(ExtProcessInstancePO::getId, e -> e));
        historicProcessInstances.forEach(historicProcessInstance -> {
            List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(historicProcessInstance.getId()).variableName(VariableConstant.RESULT)
                    .orderBy(new HistoricVariableInstanceQueryProperty(
                            "REV_"
                    )).desc().list();
            Object value = null;
            if (CollectionUtils.isNotEmpty(historicVariableInstances)) {
                value = historicVariableInstances.get(0).getValue();
            }

            Object status = value;
            if (Objects.isNull(historicProcessInstance.getEndTime())) {
                status = ProcessStatus.ACTIVE.name();
            }
            String currentTaskId = null;
            List<Task> taskList = instanceToTasksMap.get(historicProcessInstance.getId());
            if (CollectionUtils.isNotEmpty(taskList)) {
                currentTaskId = taskList.getFirst().getId();
            }
            LocalDateTime endTime = DateUtils.parse(historicProcessInstance.getEndTime());
            if (Objects.nonNull(status) && status.equals(TaskOperationType.PASS.name()) && Objects.nonNull(endTime)) {
                status = "FINISHED";
            }
            List<HistoricVariableInstance> formNos = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(historicProcessInstance.getId()).variableName(VariableConstant.FORM_NO).list();
            String formNo = "";
            if (CollectionUtils.isNotEmpty(formNos)) {
                formNo = formNos.get(0).getValue().toString();
            }
            Map<String, Object> variables = new HashMap<>();
            variables.put(VariableConstant.getVariableName(VariableConstant.DATA), dataMap.get(historicProcessInstance.getId()));
            variables.put(VariableConstant.getVariableName(VariableConstant.FORM_NO), formNo);
            variables.put(VariableConstant.getVariableName(VariableConstant.FORM_INSTANCE_Id), processInstanceQueryService.getFormInstanceIdByProcessInstanceId(historicProcessInstance.getId()));
            result.add(ProcessInstanceDTO.builder()
                               .submitedTime(DateUtils.parse(historicProcessInstance.getStartTime()))
                               .processInstanceId(historicProcessInstance.getId())
                               .instanceName(startUserInfoMap.get(historicProcessInstance.getId()).getInstanceName())
                               .startUserId(Long.parseLong(historicProcessInstance.getStartUserId()))
                               .currentTaskId(currentTaskId)
                               .formNo(formNo)
                               .startUserInfo(startUserInfoMap.get(historicProcessInstance.getId()).getStartUserInfo())
                               .status(Objects.isNull(status) ? null : status.toString())
                               .startUserName(userIdNameMap.get(Long.valueOf(historicProcessInstance.getStartUserId())))
                               .formInstanceId(processInstanceQueryService.getFormInstanceIdByProcessInstanceId(historicProcessInstance.getId()))
                               .activeNodeName(Objects.isNull(activeNodeDTOMap.get(historicProcessInstance.getId())) ? "-"
                                                                                                                     : activeNodeDTOMap
                                                       .get(historicProcessInstance.getId()).getName())
                               .variables(variables)
                               .build());
        });

        return Pagination.<ProcessInstanceDTO>builder().total(totalCount)
                .pages((totalCount + query.getPageSize() - 1) / query.getPageSize())
                .records(result).build();

    }

    @GetMapping("/process/instance/historical/page/all")
    public Pagination<ProcessInstanceDTO> page(@Valid PageOfAllDTO query) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Set<String> processInstanceIds = processInstanceQueryService.getProcessInstanceIdByBusinessTypeAndPeriod(query.getBusinessType(), null);
        if (CollectionUtils.isEmpty(processInstanceIds)) {
            return Pagination.empty();
        }

        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .orderByProcessInstanceStartTime().desc();
        if (Objects.nonNull(query.getSubmitPeriod())) {
            historicProcessInstanceQuery.startedAfter(DateConvert.convertToDate(query.getSubmitPeriod().getBegin()))
                    .startedBefore(DateConvert.convertToDate(query.getSubmitPeriod().getEnd()));
        }

        if (Objects.nonNull(query.getCompletePeriod())) {
            historicProcessInstanceQuery.finishedAfter(DateConvert.convertToDate(query.getCompletePeriod().getBegin()))
                    .finishedBefore(DateConvert.convertToDate(query.getCompletePeriod().getEnd()));
        }

        if (StringUtils.isNotEmpty(query.getStatus())) {
            if (query.getStatus().equals("ACTIVE")) {
                historicProcessInstanceQuery
                        .unfinished();
            } else if (query.getStatus().equals("FINISHED")) {
                List<ExtProcessInstancePO> extProcessInstancePOS = extProcessInstanceMapper.selectList(
                        Wrappers.<ExtProcessInstancePO>lambdaQuery()
                                .eq(ExtProcessInstancePO::getResult, TaskOperationType.PASS.name())
                                .in(ExtProcessInstancePO::getId, processInstanceIds)
                                                                                                      );
                if (CollectionUtils.isEmpty(extProcessInstancePOS)) {
                    return Pagination.empty();
                } else {
                    historicProcessInstanceQuery
                            .processInstanceIds(extProcessInstancePOS.stream().map(ExtProcessInstancePO::getId).collect(Collectors.toSet()))
                            .finished();
                    processInstanceIds = extProcessInstancePOS.stream().map(ExtProcessInstancePO::getId).collect(Collectors.toSet());
                }
            } else {
                List<ExtProcessInstancePO> extProcessInstancePOS = extProcessInstanceMapper.selectList(
                        Wrappers.<ExtProcessInstancePO>lambdaQuery()
                                .eq(ExtProcessInstancePO::getResult, query.getStatus())
                                .in(ExtProcessInstancePO::getId, processInstanceIds)
                                                                                                      );
                if (CollectionUtils.isEmpty(extProcessInstancePOS)) {
                    return Pagination.empty();
                } else {
                    processInstanceIds = extProcessInstancePOS.stream().map(ExtProcessInstancePO::getId).collect(Collectors.toSet());
                    historicProcessInstanceQuery
                            .processInstanceIds(processInstanceIds)
                            .finished();
                }
            }
        } else {
            historicProcessInstanceQuery.processInstanceIds(processInstanceIds);
        }

        long totalCount = historicProcessInstanceQuery.processInstanceIds(processInstanceIds).count();

        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.listPage(
                (query.getPageNumber() - 1) * query.getPageSize(), query.getPageSize()
                                                                                                      );

        if (CollectionUtils.isEmpty(historicProcessInstances)) {
            return Pagination.empty();
        }

        Map<String, ActiveNodeDTO> activeNodeDTOMap = taskInstanceQueryService.listActiveNode(
                        historicProcessInstances.stream().map(HistoricProcessInstance::getId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(ActiveNodeDTO::getProcessInstanceId, value -> value));

        Map<Long, String> userIdNameMap = userClient.listUserByIds(historicProcessInstances.stream()
                                                                           .map(processInstance -> Long.valueOf(processInstance.getStartUserId()))
                                                                           .collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        Map<String, List<Task>> instanceToTasksMap = taskService.createTaskQuery().active().processInstanceIdIn(
                        historicProcessInstances.stream().map(HistoricProcessInstance::getId).collect(Collectors.toList())
                                                                                                               )
                .taskCandidateOrAssigned(userId.toString()).list().stream().collect(Collectors.groupingBy(Task::getProcessInstanceId));

        List<ProcessInstanceDTO> result = new ArrayList<>();
        Map<String, Object> dataMap = historyService.createHistoricVariableInstanceQuery()
                .executionIds(historicProcessInstances.stream().map(HistoricProcessInstance::getId).collect(Collectors.toSet()))
                .variableName(VariableConstant.DATA)
                .list().stream()
                .collect(Collectors.toMap(HistoricVariableInstance::getProcessInstanceId, HistoricVariableInstance::getValue));

          Map<String, ExtProcessInstancePO> startUserInfoMap = extProcessInstanceMapper.selectList(
                        Wrappers.<ExtProcessInstancePO>lambdaQuery()
                                .in(ExtProcessInstancePO::getId, historicProcessInstances.stream().map(HistoricProcessInstance::getId)
                                        .collect(Collectors.toSet()))
                                                                                                ).stream().filter(Objects::nonNull)
                .collect(Collectors.toMap(ExtProcessInstancePO::getId, e -> e));

        historicProcessInstances.forEach(historicProcessInstance -> {
            List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(historicProcessInstance.getId()).variableName(VariableConstant.RESULT)
                    .orderBy(new HistoricVariableInstanceQueryProperty(
                            "REV_"
                    )).desc().list();
            Object value = null;
            if (CollectionUtils.isNotEmpty(historicVariableInstances)) {
                value = historicVariableInstances.get(0).getValue();
            }

            Object status = value;
            if (Objects.isNull(historicProcessInstance.getEndTime())) {
                status = ProcessStatus.ACTIVE.name();
            }
            String currentTaskId = null;
            List<Task> taskList = instanceToTasksMap.get(historicProcessInstance.getId());
            if (CollectionUtils.isNotEmpty(taskList)) {
                currentTaskId = taskList.get(0).getId();
            }

            LocalDateTime endTime = DateUtils.parse(historicProcessInstance.getEndTime());
            if (Objects.nonNull(status) && status.equals(TaskOperationType.PASS.name()) && Objects.nonNull(endTime)) {
                status = "FINISHED";
            }
            List<HistoricVariableInstance> formHistoricVariableInstances = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(historicProcessInstance.getId()).variableName(VariableConstant.FORM_NO).list();
            Object formNo = null;
            if (CollectionUtils.isNotEmpty(formHistoricVariableInstances)) {
                formNo = formHistoricVariableInstances.get(0).getValue();
            }
            Map<String, Object> variables = new HashMap<>();
            variables.put(VariableConstant.getVariableName(VariableConstant.DATA), dataMap.get(historicProcessInstance.getId()));
            variables.put(VariableConstant.getVariableName(VariableConstant.FORM_NO), formNo);
            variables.put(VariableConstant.getVariableName(VariableConstant.FORM_INSTANCE_Id), processInstanceQueryService.getFormInstanceIdByProcessInstanceId(historicProcessInstance.getId()));
            result.add(ProcessInstanceDTO.builder()
                               .currentTaskId(currentTaskId)
                               .startUserInfo(startUserInfoMap.get(historicProcessInstance.getId()).getStartUserInfo())
                               .instanceName(startUserInfoMap.get(historicProcessInstance.getId()).getInstanceName())
                               .processInstanceId(historicProcessInstance.getId())
                               .submitedTime(DateUtils.parse(historicProcessInstance.getStartTime()))
                               .startUserId(Long.parseLong(historicProcessInstance.getStartUserId()))
                               .formNo(formNo)
                               .status(Objects.isNull(status) ? null : status.toString())
                               .startUserName(userIdNameMap.get(Long.valueOf(historicProcessInstance.getStartUserId())))
                               .formInstanceId(processInstanceQueryService.getFormInstanceIdByProcessInstanceId(historicProcessInstance.getId()))
                               .activeNodeName(Objects.isNull(activeNodeDTOMap.get(historicProcessInstance.getId())) ? "-"
                                                                                                                     : activeNodeDTOMap
                                                       .get(historicProcessInstance.getId()).getName())
                               .variables(variables)
                               .build());
        });

        return Pagination.<ProcessInstanceDTO>builder().total(totalCount)
                .pages((totalCount + query.getPageSize() - 1) / query.getPageSize())
                .records(result).build();

    }

    /**
     * @Author wql
     * @Description 查询流程需要打印的节点信息
     * @Date 16:15 2023/7/25
     **/
    @GetMapping("/process/print/node")
    public List<TaskLog> getFlowPrintNodeInfo(String processInstanceId) {
        Assert.isParameterError(processInstanceId == null, "流程实例id不允许为空！");
        return processInstanceQueryService.getFlowPrintNodeInfo(processInstanceId);
    }

    /**
     * 历史流程路径（线性）
     */
    @GetMapping("/process/historical/path/line")
    public List<TaskLog> getHistoricLinePath(@RequestParam(required = false, name = "processInstanceId") String processInstanceId,
                                             @RequestParam(name = "flowKey", required = false) String flowKey
                                            ) throws Exception {
        return processInstanceQueryService.getHistoricLinePath(processInstanceId, flowKey);
    }

    /**
     * 流程审批记录导出
     *
     * @param pageQuery
     * @param response
     */
    @GetMapping("/process/instance/historical/export")
    public void processInstanceHistoricalExport(@Valid ProcessInstanceHistoricalQuery pageQuery, HttpServletResponse response) {
        taskInstanceQueryService.processInstanceHistoricalExport(pageQuery, response);
    }

    /**
     * 流程审批记录批量导出
     */
    @PostMapping("/process/instance/historical/export/batch")
    public void processInstanceHistoricalExportBatch(@Valid @RequestBody ProcessInstanceHistoricalQuery pageQuery,
                                                     HttpServletResponse response
                                                    ) {
        taskInstanceQueryService.processInstanceHistoricalExport(pageQuery, response);
    }

    @PostMapping("/process/historical/node")
    public Map<String, List<TaskLogVO>> getHistoricNode(@RequestBody List<String> processInstanceIdList) {
        return taskInstanceQueryService.getHistoricNode(processInstanceIdList);
    }


}
