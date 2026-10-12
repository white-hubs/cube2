package com.guhecloud.cube.base.flow.flowable.task.application.querygateway;

import base.constant.VariableConstant;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.base.file.client.util.CustomCellWriteUtil;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po.ProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.process.application.dto.ActiveNodeDTO;
import com.guhecloud.cube.base.flow.flowable.process.application.enumeration.FlowResult;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.AllFlowOrderPageQuery;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.ProcessInstanceHistoricalQuery;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.ProcessInstancePageQuery;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.BacklogKey;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.ProcessInstanceExportDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.TaskLogVO;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.CopyLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskLogPO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/9/2 16:01
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskInstanceQueryService {

    private static final int PERSONAL_EXPORT_PAGE_SIZE = 2000;

    private final TaskService taskService;
    private final HistoryService historyService;
    private final ProcessInstanceMapper processInstanceMapper;
    private final CopyLogMapper copyLogMapper;
    private final ExtProcessInstanceMapper extProcessInstanceMapper;

    public Set<Long> getTaskEndAssignees(String processInstanceId) {
        Set<Long> assigneeUserIds = new HashSet<>();
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .taskVariableValueEquals(VariableConstant.TASK_RESULT, TaskOperationType.PASS.name())
                .list();
        if (CollectionUtils.isNotEmpty(historicTaskInstances)) {
            historicTaskInstances.forEach(historicTaskInstance -> {
                String assignee = historicTaskInstance.getAssignee();
                assigneeUserIds.add(Long.valueOf(assignee));
            });
        }

        return assigneeUserIds;
    }

    public Set<Long> getActivatedTaskAssignees(String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery().active().processInstanceId(processInstanceId).list();
        Set<Long> assigneeUserIds = new HashSet<>();
        tasks.forEach(task -> {
            // 获取催办规则-针对流程中的每个节点，一天只能催办一次
            String assignee = task.getAssignee();
            assigneeUserIds.add(Long.valueOf(assignee));
        });

        return assigneeUserIds;
    }

    public List<ActiveNodeDTO> listActiveNode(Set<String> processInstanceIds) {
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceIdIn(new ArrayList<>(processInstanceIds)).active().list();

        if (CollectionUtils.isEmpty(tasks)) {
            return Collections.emptyList();
        }

        // 获取所有流程中的活跃节点名称
        Map<String, Task> activeNodeNameMap = tasks.stream()
                .collect(Collectors.toMap(Task::getProcessInstanceId, task -> task, (o, n) -> o));

        List<ActiveNodeDTO> result = new ArrayList<>();

        Iterator<Map.Entry<String, Task>> iterator = activeNodeNameMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Task> next = iterator.next();
            String key = next.getKey();
            Task task = next.getValue();

            result.add(new ActiveNodeDTO(task.getName(), task.getTaskDefinitionKey(), key));
        }

        return result;
    }

    /**
     * @Author wql
     * @Description 流程实例列表
     * @Date 17:30 2023/5/23
     **/
    public Pagination<ProcessInstanceDTO> page(ProcessInstancePageQuery query) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        if (BacklogKey.COPY_OF_ME.getValue().equals(query.getType())) {
            return this.copePage(query);
        } else if (BacklogKey.SUBMITTED_OF_ME.getValue().equals(query.getType())) {
            query.setStartUserId(userId.toString());
            return this.initiatedPage(query);
        } else if (BacklogKey.BACKLOG_OF_ME.getValue().equals(query.getType())) {
            return this.taskPage(query);
        } else if (BacklogKey.PROCESSED_OF_ME.getValue().equals(query.getType())) {
            return this.taskPage(query);
        }
        query.setAssignee(ObjectUtils.isNotEmpty(query.getType()) ? userId.toString() : null);
        if (FlowResult.FINISHED.name().equals(query.getStatus())) {
            query.setStatus(FlowResult.PASS.name());
        }
        Page<String> page = new Page<>(query.getPageNumber(), query.getPageSize());
        page = extProcessInstanceMapper.getProcessInstanceIdsByUser(page, query);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || 0 == page.getRecords().size()) {
            return PagingUtils.convert(page, new ArrayList<>());
        }
        query.setProcessInstanceIdsList(page.getRecords());
        query.setPageNumber(1);
        Pagination<ProcessInstanceDTO> instanceList = this.initiatedPage(query);
        return PagingUtils.convert(page, instanceList.getRecords());
    }

    /**
     * 抄送列表
     */
    public Pagination<ProcessInstanceDTO> copePage(ProcessInstancePageQuery query) {
        if (FlowResult.FINISHED.name().equals(query.getStatus())) {
            query.setStatus(FlowResult.PASS.name());
        }
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Page page = new Page<>(query.getPageNumber(), query.getPageSize());
        Page<ProcessInstanceDTO> pageData = copyLogMapper.selectListByUserIdPage(page, query, userId.toString());
        //获取变量值
        if (ObjectUtils.isNull(pageData) || CollectionUtils.isEmpty(pageData.getRecords())) {
            return new Pagination<>();
        }
        Map<String, Map<String, Object>> resultMap = this.getHistoricVariable(pageData.getRecords().stream()
                                                                                      .map(ProcessInstanceDTO::getProcessInstanceId)
                                                                                      .collect(Collectors.toSet()));
        pageData.getRecords().forEach(item -> {
            item.setStartUserInfo(new StartUserInfo(item.getStartUserId(), item.getStartUserName(), item.getOrganizationId(), item.getOrganizationName(), null));
            item.setStatus(this.getInstanceState(item.getStatus()));
            Map<String, Object> variableMap = resultMap.getOrDefault(item.getProcessInstanceId(), new HashMap<>());
            item.setVariables(assembleVariables(variableMap));
        });
        return PagingUtils.convert(page, pageData.getRecords());
    }

    /**
     * 代办列表
     */
    public Pagination<ProcessInstanceDTO> taskPage(ProcessInstancePageQuery query) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Page page = new Page<>(query.getPageNumber(), query.getPageSize());
        Page<ProcessInstanceDTO> pageData = extProcessInstanceMapper.selectTaskPage(page, query, userId.toString());
        //获取变量值
        if (ObjectUtils.isNull(pageData) || CollectionUtils.isEmpty(pageData.getRecords())) {
            return new Pagination<>();
        }
        Map<String, Map<String, Object>> resultMap = this.getHistoricVariable(pageData.getRecords().stream()
                                                                                      .map(ProcessInstanceDTO::getProcessInstanceId)
                                                                                      .collect(Collectors.toSet()));
        pageData.getRecords().forEach(item -> {
            item.setStartUserInfo(new StartUserInfo(item.getStartUserId(), item.getStartUserName(), item.getOrganizationId(), item.getOrganizationName(), null));
            item.setStatus(this.getInstanceState(item.getStatus()));
            Map<String, Object> variableMap = resultMap.getOrDefault(item.getProcessInstanceId(), new HashMap<>());
            item.setVariables(assembleVariables(variableMap));
        });
        return PagingUtils.convert(page, pageData.getRecords());
    }

    /**
     * 发起列表
     */
    public Pagination<ProcessInstanceDTO> initiatedPage(ProcessInstancePageQuery query) {
        if (FlowResult.FINISHED.name().equals(query.getStatus())) {
            query.setStatus(FlowResult.PASS.name());
        }
        Page page = new Page<>(query.getPageNumber(), query.getPageSize());
        Page<ProcessInstanceDTO> pageData = extProcessInstanceMapper.selectInitiatedPage(page, query);
        //获取变量值
        if (ObjectUtils.isNull(pageData) || CollectionUtils.isEmpty(pageData.getRecords())) {
            return new Pagination<>();
        }
        Map<String, Map<String, Object>> resultMap = this.getHistoricVariable(pageData.getRecords().stream()
                                                                                      .map(ProcessInstanceDTO::getProcessInstanceId)
                                                                                      .collect(Collectors.toSet()));
        pageData.getRecords().forEach(item -> {
            item.setStartUserInfo(new StartUserInfo(item.getStartUserId(), item.getStartUserName(), item.getOrganizationId(), item.getOrganizationName(), null));
            item.setStatus(this.getInstanceState(item.getStatus()));
            if (!FlowResult.ACTIVE.name().equals(item.getStatus())) {
                item.setActiveNodeName(FlowResult.FINISHED.getDesc());
            }
            Map<String, Object> variableMap = resultMap.getOrDefault(item.getProcessInstanceId(), new HashMap<>());
            item.setVariables(assembleVariables(variableMap));
        });
        return PagingUtils.convert(page, pageData.getRecords());
    }

    private LambdaQueryWrapper<ProcessInstancePO> addCommonCondition(LambdaQueryWrapper<ProcessInstancePO> wrapper,
                                                                     ProcessInstancePageQuery query, Integer tenant
                                                                    ) {
        List<String> procInstanceIds = StringUtils.isBlank(query.getProcessInstanceIds()) ? Collections.emptyList()
                                                                                          : Arrays.asList(query.getProcessInstanceIds()
                                                                                                                  .split(","));
        wrapper.eq(ProcessInstancePO::getTenant, tenant)
                .like(StringUtils.isNotBlank(query.getUsername()), ProcessInstancePO::getStartUserName, query.getUsername())
                .eq(StringUtils.isNotBlank(query.getStatus()) &&
                    !FlowResult.FINISHED.name().equals(query.getStatus()), ProcessInstancePO::getState, query.getStatus())
                .eq(FlowResult.FINISHED.name().equals(query.getStatus()), ProcessInstancePO::getState, FlowResult.PASS.name())
                .eq(StringUtils.isNotBlank(query.getBusinessType()), ProcessInstancePO::getBusinessType, query.getBusinessType())
                .eq(StringUtils.isNotBlank(query.getApplicationCode()), ProcessInstancePO::getBusinessModel, query.getApplicationCode())
                .eq(ObjectUtils.isNotEmpty(query.getStartUserId()), ProcessInstancePO::getStartUserId, query.getStartUserId())
                .eq(ObjectUtils.isNotEmpty(query.getOrganizationId()), ProcessInstancePO::getOrganizationId, query.getOrganizationId())
                .between(ObjectUtils.isNotEmpty(query.getSubmitTime()), ProcessInstancePO::getStartTime,
                         null != query.getSubmitTime() ? query.getSubmitTime().getBegin() : null,
                         query.getSubmitTime() != null ? query.getSubmitTime().getEnd() : null)
                .in(ObjectUtils.isNotEmpty(procInstanceIds), ProcessInstancePO::getProcInstId, procInstanceIds)
                .groupBy(ProcessInstancePO::getProcInstId)
                .orderByDesc(ProcessInstancePO::getStartTime);
        return wrapper;
    }

    /**
     * @Author wql
     * @Description 组装变量信息
     * @Date 15:22 2023/5/24
     **/
    private Map<String, Object> assembleVariables(Map<String, Object> map) {
        Map<String, Object> variables = new HashMap<>();
        if (ObjectUtils.isNotEmpty(map)) {
            variables.put(VariableConstant.getVariableName(VariableConstant.DATA), map.get(VariableConstant.DATA));
            variables.put(VariableConstant.getVariableName(VariableConstant.FORM_NO), map.get(VariableConstant.FORM_NO));
            variables.put(VariableConstant.getVariableName(VariableConstant.FORM_INSTANCE_Id), map.get(VariableConstant.FORM_INSTANCE_Id));
        }
        return variables;
    }

    private Map<String, HistoricTaskInstance> getAllCurrentNode(Set<String> collect) {
        List<String> list = new ArrayList<>(collect);
        Map<String, HistoricTaskInstance> map = new HashMap<>();
        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery().
                processInstanceIdIn(list).orderByHistoricTaskInstanceStartTime().desc().list();
        if (ObjectUtils.isNotEmpty(taskInstances)) {
            for (HistoricTaskInstance instance : taskInstances) {
                if (!map.containsKey(instance.getProcessInstanceId())) {
                    map.put(instance.getProcessInstanceId(), instance);
                }
            }
        }
        return map;
    }

    private Map<String, Map<String, Object>> getHistoricVariable(Set<String> collect) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().executionIds(collect).list();
        if (ObjectUtils.isNotEmpty(list)) {
            for (HistoricVariableInstance instance : list) {
                if (map.containsKey(instance.getProcessInstanceId())) {
                    Map<String, Object> sonMap = map.get(instance.getProcessInstanceId());
                    sonMap.put(instance.getVariableName(), instance.getValue());
                } else {
                    Map<String, Object> sonMap = new HashMap<>();
                    sonMap.put(instance.getVariableName(), instance.getValue());
                    map.put(instance.getProcessInstanceId(), sonMap);
                }
            }
        }
        return map;
    }

    private String getInstanceState(String state) {
        if (StringUtils.isBlank(state)) {
            state = FlowResult.ACTIVE.name();
        }
        return FlowResult.PASS.name().equals(state) ? FlowResult.FINISHED.name() : state;
    }

    /**
     * @Author wql
     * @Description 已处理列表
     * @Date 10:24 2023/6/29
     **/
    public Pagination<ProcessInstanceDTO> handlePage(ProcessInstancePageQuery query, Integer tenant) {
        Page<ProcessInstancePO> page = new Page<>(query.getPageNumber(), query.getPageSize());
        page = processInstanceMapper.handlePage(page, query, tenant);
        return PagingUtils.convert(page, fieldCover(page));
    }

    /**
     * @Author wql
     * @Description 列表字段转换
     * @Date 10:25 2023/6/29
     **/
    private List<ProcessInstanceDTO> fieldCover(Page<ProcessInstancePO> page) {
        List<ProcessInstanceDTO> instances = new ArrayList<>();
        List<ProcessInstancePO> pos = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList());
        if (ObjectUtils.isNotEmpty(pos)) {
            Set<String> collect = pos.stream().map(ProcessInstancePO::getProcInstId).collect(Collectors.toSet());
            log.info("procInstIds:{}", JsonUtils.serialize(collect));
            //获取所有流程的结果信息
            Map<String, Map<String, Object>> resultMap = getHistoricVariable(collect);
            //获取所有流程的当前节点
            Map<String, HistoricTaskInstance> currentNode = getAllCurrentNode(collect);
            for (ProcessInstancePO po : pos) {
                StartUserInfo startUserInfo = JsonUtils.deserialize(po.getStartUserInfo(), StartUserInfo.class);
                Map<String, Object> map = resultMap.get(po.getProcInstId());
                map = ObjectUtils.isEmpty(map) ? new HashMap<>() : map;
                JsonNode userInfo = JsonUtils.deserialize(String.valueOf(map.get(VariableConstant.START_USER_INFO)));
                Object instanceId = map.get(VariableConstant.FORM_INSTANCE_Id);
                String state = getInstanceState(po.getState());
                HistoricTaskInstance instance = FlowResult.ACTIVE.name().equals(state) ? currentNode.get(po.getProcInstId()) : null;
                instances.add(ProcessInstanceDTO.builder()
                                      .formInstanceId(ObjectUtils.isEmpty(instanceId) ? null : Long.valueOf(String.valueOf(instanceId)))
                                      .formNo(map.get(VariableConstant.FORM_NO))
                                      .processInstanceId(po.getProcInstId())
                                      .startUserId(Long.parseLong(po.getStartUserId()))
                                      .startUserInfo(startUserInfo)
                                      .startUserName(ObjectUtils.isNull(userInfo) ? "" : JsonUtils.getNodeValue(userInfo, "name"))
                                      .submitedTime(po.getStartTime())
                                      .status(state)
                                      .activeNodeName(ObjectUtils.isNull(instance) ? "" : instance.getName())
                                      .activeNodeAssignee(ObjectUtils.isNull(instance) ? "" : instance.getAssignee())
                                      .instanceName(po.getApplyName())
                                      .variables(assembleVariables(map))
                                      .businessType(po.getBusinessType())
                                      .build());
            }
        }
        return instances;
    }

    /**
     * @Author wql
     * @Description 待处理列表
     * @Date 14:38 2023/6/29
     **/
    public Pagination<ProcessInstanceDTO> waitingPage(ProcessInstancePageQuery query, Integer tenant) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Period submitTime = query.getSubmitTime();
        Page<ProcessInstancePO> page = processInstanceMapper.selectPage(PagingUtils.convert(query), Wrappers
                                                                                .<ProcessInstancePO>lambdaQuery()
                                                                                .eq(ProcessInstancePO::getTenant, tenant)
                                                                                .eq(ProcessInstancePO::getAssignee, userId.toString())
                                                                                .eq(ProcessInstancePO::getState, FlowResult.ACTIVE.name())
                                                                                .eq(StringUtils.isNotBlank(query.getBusinessType()), ProcessInstancePO::getBusinessType, query.getBusinessType())
                                                                                .eq(StringUtils.isNotBlank(query.getApplicationCode()), ProcessInstancePO::getBusinessModel, query.getApplicationCode())
                                                                                .between(ObjectUtils.isNotEmpty(submitTime), ProcessInstancePO::getTaskCreateTime,
                                                                                         null != submitTime ? submitTime.getBegin() : null, submitTime != null ? submitTime.getEnd() : null)
                                                                                .groupBy(ProcessInstancePO::getProcInstId)
                                                                                .orderByDesc(ProcessInstancePO::getStartTime)
                                                                       );
        return PagingUtils.convert(page, fieldCover(page));
    }

    /**
     * @Author wql
     * @Description 我发起的列表
     * @Date 14:42 2023/6/29
     **/
    public Pagination<ProcessInstanceDTO> submitedPage(ProcessInstancePageQuery query, Integer tenant) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Period submitTime = query.getSubmitTime();
        Page<ProcessInstancePO> page = processInstanceMapper.selectPage(PagingUtils.convert(query), Wrappers
                                                                                .<ProcessInstancePO>lambdaQuery()
                                                                                .eq(ProcessInstancePO::getTenant, tenant)
                                                                                .eq(ProcessInstancePO::getStartUserId, userId.toString())
                                                                                .eq(StringUtils.isNotBlank(query.getBusinessType()), ProcessInstancePO::getBusinessType, query.getBusinessType())
                                                                                .eq(StringUtils.isNotBlank(query.getApplicationCode()), ProcessInstancePO::getBusinessModel, query.getApplicationCode())
                                                                                .eq(FlowResult.FINISHED.name().equals(query.getStatus()), ProcessInstancePO::getState, FlowResult.PASS.name())
                                                                                .eq(StringUtils.isNotBlank(query.getStatus()) &&
                                                                                    !(FlowResult.FINISHED.name().equals(query.getStatus())), ProcessInstancePO::getState, query.getStatus())
                                                                                .between(ObjectUtils.isNotEmpty(submitTime), ProcessInstancePO::getStartTime,
                                                                                         null != submitTime ? submitTime.getBegin() : null, submitTime != null ? submitTime.getEnd() : null)
                                                                                .groupBy(ProcessInstancePO::getProcInstId)
                                                                                .orderByDesc(ProcessInstancePO::getStartTime)
                                                                       );
        return PagingUtils.convert(page, fieldCover(page));
    }

    /**
     * @Author wql
     * @Description 所有流程实例列表
     * @Date 11:56 2023/7/12
     **/
    public Pagination<ProcessInstanceDTO> allPage(ProcessInstancePageQuery query) {
        if (ObjectUtils.isNotEmpty(query.getProcessInstanceIds())) {
            query.setProcessInstanceIdsList(Arrays.stream(query.getProcessInstanceIds().split(","))
                                                    .collect(Collectors.toList()));
            return this.initiatedPage(query);
        }
        query.setType(null);
        return this.page(query);
    }

    public Pagination<ProcessInstanceDTO> flowOrderAll(AllFlowOrderPageQuery query) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        LambdaQueryWrapper<ProcessInstancePO> wrapper = Wrappers.lambdaQuery();
        List<String> businessTypes =
                StringUtils.isBlank(query.getBusinessTypes()) ? new ArrayList<>() : Arrays.asList(query.getBusinessTypes().split(","));
        List<String> applicationCodes =
                StringUtils.isBlank(query.getApplicationCodes()) ? new ArrayList<>()
                                                                 : Arrays.asList(query.getApplicationCodes().split(","));
        List<String> finished = Arrays.asList(FlowResult.PASS.name(), FlowResult.UNDO.name(), FlowResult.REJECT.name());
        wrapper.eq(ProcessInstancePO::getTenant, tenant)
                .eq(FlowResult.ACTIVE.name().equals(query.getStatus()), ProcessInstancePO::getState, query.getStatus())
                .in(FlowResult.FINISHED.name().equals(query.getStatus()), ProcessInstancePO::getState, finished)
                .in(ObjectUtils.isNotEmpty(businessTypes), ProcessInstancePO::getBusinessType, businessTypes)
                .in(ObjectUtils.isNotEmpty(applicationCodes), ProcessInstancePO::getBusinessModel, applicationCodes)
                .groupBy(ProcessInstancePO::getProcInstId);
        Page<ProcessInstancePO> page = processInstanceMapper.selectPage(PagingUtils.convert(query), wrapper);
        return PagingUtils.convert(page, fieldCover(page));
    }

    public void processInstanceHistoricalExport(ProcessInstanceHistoricalQuery query, HttpServletResponse response) {
        //参数转换
        if (FlowResult.FINISHED.name().equals(query.getStatus())) {
            query.setStatus(FlowResult.PASS.name());
        }
        List<String> processInstanceIds;
        if (CollectionUtils.isNotEmpty(query.getProcessInstanceIdsList())) {
            processInstanceIds = query.getProcessInstanceIdsList();
        } else if (ObjectUtils.isNotEmpty(query.getProcessInstanceIds())) {
            processInstanceIds = Arrays.stream(query.getProcessInstanceIds().split(","))
                    .collect(Collectors.toList());
        } else {
            processInstanceIds = extProcessInstanceMapper.getProcessInstanceIds(query);
        }
        Assert.isNotFound(CollectionUtils.isEmpty(processInstanceIds), "没有流程审批记录,无法导出");

        query.setProcessInstanceIdsList(processInstanceIds);

        List<ProcessInstanceDTO> processInstanceDTOS = extProcessInstanceMapper.selectInitiatedPage(query);
        writeProcessInstanceHistoricalExcel(processInstanceDTOS, query.getExportName(), response);
    }

    /**
     * 个人流程列表（{@link #page(ProcessInstancePageQuery)}）导出，与 {@link #processInstanceHistoricalExport} 相同表头，数据范围与 page 一致
     */
    public void processInstancePersonalHistoricalExport(ProcessInstancePageQuery query, String exportName, HttpServletResponse response) {
        ProcessInstancePageQuery q = copyProcessInstancePageQuery(query);
        if (FlowResult.FINISHED.name().equals(q.getStatus())) {
            q.setStatus(FlowResult.PASS.name());
        }
        Long userId = SecurityContextHolder.getPrincipal().getId();
        List<ProcessInstanceDTO> rows;
        if (BacklogKey.COPY_OF_ME.getValue().equals(q.getType())) {
            rows = fetchAllPersonalExportPages(this::copePage, q);
        } else if (BacklogKey.SUBMITTED_OF_ME.getValue().equals(q.getType())) {
            q.setStartUserId(userId.toString());
            rows = fetchAllPersonalExportPages(this::initiatedPage, q);
        } else if (BacklogKey.BACKLOG_OF_ME.getValue().equals(q.getType()) ||
                   BacklogKey.PROCESSED_OF_ME.getValue().equals(q.getType())) {
            rows = fetchAllPersonalExportPages(this::taskPage, q);
        } else {
            q.setAssignee(ObjectUtils.isNotEmpty(q.getType()) ? userId.toString() : null);
            List<String> ids = extProcessInstanceMapper.listProcessInstanceIdsByUser(q);
            Assert.isNotFound(CollectionUtils.isEmpty(ids), "没有流程审批记录,无法导出");
            ProcessInstanceHistoricalQuery h = toHistoricalQuery(q);
            h.setProcessInstanceIdsList(ids);
            rows = extProcessInstanceMapper.selectInitiatedPage(h);
        }
        writeProcessInstanceHistoricalExcel(rows, exportName, response);
    }

    public Map<String, List<TaskLogVO>> getHistoricNode(List<String> processInstanceIdList) {
        if (CollectionUtils.isEmpty(processInstanceIdList)) {
            return new HashMap<>();
        }
        TaskLogMapper taskLogMapper = ApplicationContextHolder.getBean(TaskLogMapper.class);
        List<TaskLogPO> taskLogPOS = taskLogMapper.selectList(
                Wrappers.<TaskLogPO>lambdaQuery()
                        .in(TaskLogPO::getProcessInstanceId, processInstanceIdList)
                        .orderByAsc(TaskLogPO::getCreateTime));

        if (CollectionUtils.isEmpty(taskLogPOS)) {
            return new HashMap<>();
        }
        Map<String, List<TaskLogVO>> result = new HashMap<>();
        Map<String, List<TaskLogPO>> groupByProcessInstancIdMap = taskLogPOS.stream()
                .collect(Collectors.groupingBy(TaskLogPO::getProcessInstanceId));
        for (Map.Entry<String, List<TaskLogPO>> entry : groupByProcessInstancIdMap.entrySet()) {
            List<TaskLogVO> taskLogPOList = new ArrayList<>();
            List<TaskLogPO> processTaskLogs = entry.getValue().stream()
                    .sorted(Comparator.comparing(TaskLogPO::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
            for (TaskLogPO taskLogPO : processTaskLogs) {
                TaskLog taskLog = JsonUtils.deserialize(taskLogPO.getOperationInfo(), TaskLog.class);
                TaskLogVO taskLogVO = new TaskLogVO();
                taskLogVO.setProcessInstanceId(taskLogPO.getProcessInstanceId());
                taskLogVO.setNodeKey(taskLog.getNodeKey());
                taskLogVO.setNodeName(taskLog.getNodeName());

                if (CollectionUtils.isNotEmpty(taskLog.getTaskOperations())) {
                    String operatorName = taskLog.getTaskOperations().stream()
                            .filter(t -> Objects.nonNull(t.getOperation()) && Objects.nonNull(t.getOperation().getOperator()))
                            .map(t -> t.getOperation().getOperator().getName()).collect(Collectors.joining(","));
                    taskLogVO.setOperator(operatorName);
                    TaskLog.TaskOperation first = taskLog.getTaskOperations().getFirst();
                    if (Objects.nonNull(first.getOperation())) {
                        taskLogVO.setOperationTime(first.getOperation().getCreateTime());
                    }
                }
                taskLogPOList.add(taskLogVO);
            }
            result.put(entry.getKey(), taskLogPOList);
        }
        return result;
    }

    private Long calculateDurationMinutes(TaskLogPO previousTaskLogPO, TaskLogPO currentTaskLogPO) {
        if (Objects.isNull(previousTaskLogPO) || Objects.isNull(currentTaskLogPO)
            || Objects.isNull(previousTaskLogPO.getCreateTime())
            || Objects.isNull(currentTaskLogPO.getCreateTime())) {
            return 0L;
        }
        return ChronoUnit.MINUTES.between(previousTaskLogPO.getCreateTime(), currentTaskLogPO.getCreateTime());
    }

    private ProcessInstancePageQuery copyProcessInstancePageQuery(ProcessInstancePageQuery source) {
        ProcessInstancePageQuery q = new ProcessInstancePageQuery();
        q.setPageNumber(source.getPageNumber());
        q.setPageSize(source.getPageSize());
        q.setType(source.getType());
        q.setBusinessType(source.getBusinessType());
        q.setStatus(source.getStatus());
        q.setStartUserId(source.getStartUserId());
        q.setAssignee(source.getAssignee());
        q.setUsername(source.getUsername());
        q.setOrganizationId(source.getOrganizationId());
        q.setSubmitTime(source.getSubmitTime());
        q.setApplicationCode(source.getApplicationCode());
        q.setProcessInstanceIds(source.getProcessInstanceIds());
        if (source.getProcessInstanceIdsList() != null) {
            q.setProcessInstanceIdsList(new ArrayList<>(source.getProcessInstanceIdsList()));
        }
        q.setCurrentTaskName(source.getCurrentTaskName());
        return q;
    }

    private ProcessInstanceHistoricalQuery toHistoricalQuery(ProcessInstancePageQuery q) {
        ProcessInstanceHistoricalQuery h = new ProcessInstanceHistoricalQuery();
        h.setBusinessType(q.getBusinessType());
        h.setStatus(q.getStatus());
        h.setStartUserId(q.getStartUserId());
        h.setUsername(q.getUsername());
        h.setOrganizationId(q.getOrganizationId());
        h.setSubmitTime(q.getSubmitTime());
        h.setApplicationCode(q.getApplicationCode());
        h.setProcessInstanceIds(q.getProcessInstanceIds());
        return h;
    }

    private List<ProcessInstanceDTO> fetchAllPersonalExportPages(Function<ProcessInstancePageQuery, Pagination<ProcessInstanceDTO>> fetcher,
                                                                 ProcessInstancePageQuery queryTemplate
                                                                ) {
        List<ProcessInstanceDTO> out = new ArrayList<>();
        int pageNum = 1;
        while (true) {
            queryTemplate.setPageNumber(pageNum);
            queryTemplate.setPageSize(PERSONAL_EXPORT_PAGE_SIZE);
            Pagination<ProcessInstanceDTO> p = fetcher.apply(queryTemplate);
            if (p == null || CollectionUtils.isEmpty(p.getRecords())) {
                break;
            }
            out.addAll(p.getRecords());
            if (p.getRecords().size() < PERSONAL_EXPORT_PAGE_SIZE) {
                break;
            }
            pageNum++;
        }
        return out;
    }

    private void writeProcessInstanceHistoricalExcel(List<ProcessInstanceDTO> processInstanceDTOS, String exportName,
                                                     HttpServletResponse response
                                                    ) {
        Assert.isNotFound(CollectionUtils.isEmpty(processInstanceDTOS), "没有流程审批记录,无法导出");
        List<ProcessInstanceExportDTO> exportResultList = processInstanceDTOS.stream().map(item -> {
            if (!FlowResult.ACTIVE.name().equals(item.getStatus())) {
                item.setActiveNodeName(null);
            }
            ProcessInstanceExportDTO dto = new ProcessInstanceExportDTO();
            dto.setFormNo(String.valueOf(item.getFormNo()));
            dto.setInstanceName(item.getInstanceName());
            dto.setStartUserName(item.getStartUserName());
            dto.setOrganizationName(item.getOrganizationName());
            dto.setSubmitedTime(item.getSubmitedTime());
            dto.setActiveNodeName(item.getActiveNodeName());
            dto.setStatusDesc(FlowResult.valueOf(this.getInstanceState(item.getStatus())).getDesc());
            return dto;
        }).collect(Collectors.toList());
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .registerWriteHandler(new CustomCellWriteUtil())
                    .head(ProcessInstanceExportDTO.class)
                    .sheet(StringUtils.isNotBlank(exportName) ? exportName : "流程审批记录")
                    .doWrite(exportResultList);
        } catch (IOException e) {
            log.error("流程审批记录导出失败", e);
            throw new ServiceException("文件下载失败");
        }
    }

    /**
     * 批量查询流程数据
     *
     * @param query
     *
     * @return
     */
    public Pagination<ProcessInstanceDTO> allPageBatch(@Valid ProcessInstancePageQuery query) {
        if (ObjectUtils.isNotEmpty(query.getProcessInstanceIds()) || CollectionUtils.isNotEmpty(query.getProcessInstanceIdsList())) {
            List<String> processInstanceIdsList =
                    CollectionUtils.isNotEmpty(query.getProcessInstanceIdsList()) ? query.getProcessInstanceIdsList() : new ArrayList<>();
            if (ObjectUtils.isNotEmpty(query.getProcessInstanceIds())) {
                processInstanceIdsList.addAll(Arrays.asList(query.getProcessInstanceIds().split(",")));
            }
            query.setProcessInstanceIdsList(processInstanceIdsList);
            Pagination<ProcessInstanceDTO> instanceList = this.initiatedPage(query);
            return instanceList;
        }
        query.setType(null);
        return this.page(query);
    }

    public ProcessInstanceDTO processInstanceGet(String processInsId) {
        ProcessInstancePageQuery query = new ProcessInstancePageQuery();
        query.setProcessInstanceIdsList(Collections.singletonList(processInsId));
        ProcessInstanceDTO processInstanceDTO = extProcessInstanceMapper.selectInitiatedPage(query);
        if (Objects.isNull(processInstanceDTO)) {
            return null;
        }
        Map<String, Map<String, Object>> resultMap = this.getHistoricVariable(Collections.singleton(processInsId));
        processInstanceDTO.setStartUserInfo(new StartUserInfo(processInstanceDTO.getStartUserId(), processInstanceDTO.getStartUserName(), processInstanceDTO.getOrganizationId(), processInstanceDTO.getOrganizationName(), null));
        processInstanceDTO.setStatus(this.getInstanceState(processInstanceDTO.getStatus()));
        if (!FlowResult.ACTIVE.name().equals(processInstanceDTO.getStatus())) {
            processInstanceDTO.setActiveNodeName(null);
        }
        Map<String, Object> variableMap = resultMap.getOrDefault(processInstanceDTO.getProcessInstanceId(), new HashMap<>());
        processInstanceDTO.setVariables(assembleVariables(variableMap));
        return processInstanceDTO;
    }
}
