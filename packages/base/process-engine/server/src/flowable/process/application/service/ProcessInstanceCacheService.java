package com.guhecloud.cube.base.flow.flowable.process.application.service;

import base.constant.VariableConstant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.application.querygateway.ProcessDefinitionQueryService;
import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ProcessStatus;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.OperateAlias;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.CopyLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.CopyLogPO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskInstanceDetailDTO;
import com.guhecloud.cube.base.flow.form.interfaces.vo.FormInstanceDetailVO;
import com.guhecloud.cube.base.flow.form.querygateway.FormInstanceQueryService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import instance.event.ProcessWithdrawMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 流程实例信息缓存
 * @date 2023/8/22 9:32
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessInstanceCacheService {

    private final HistoryService historyService;
    private final ProcessDefinitionQueryService processDefinitionQueryService;
    private final FormInstanceQueryService formInstanceQueryService;
    private final TaskService taskService;
    private final ExtProcessInstanceMapper extProcessInstanceMapper;
    private final CopyLogMapper copyLogMapper;
    private static final String ROOT = "root";

    public TaskInstanceDetailDTO getTaskFormById(String processInstanceId, Long userId) {
        //兼容方案
        HistoricTaskInstance instance = historyService.createHistoricTaskInstanceQuery().taskId(processInstanceId).singleResult();
        if (Objects.nonNull(instance)) {
            processInstanceId = instance.getProcessInstanceId();
        }
        //获取历史流程
        long startTime = System.currentTimeMillis();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().includeProcessVariables()
                .processInstanceId(processInstanceId).singleResult();
        log.info("<=======getTaskFormById.createHistoricProcessInstanceQuery获取历史流程耗时:{}", System.currentTimeMillis() - startTime);
        if (Objects.isNull(historicProcessInstance)) {
            throw new ServiceException("流程不存在");
        }
        LocalDateTime endTime =
                Objects.nonNull(historicProcessInstance.getEndTime()) ? DateUtils.parse(historicProcessInstance.getEndTime()) : null;
        Object status = historicProcessInstance.getProcessVariables().get(VariableConstant.RESULT);
        if (Objects.isNull(endTime)) {
            status = ProcessStatus.ACTIVE.name();
        }
        if (Objects.nonNull(status) && status.equals(TaskOperationType.PASS.name()) && Objects.nonNull(endTime)) {
            status = "FINISHED";
        }
        String data = (String) historicProcessInstance.getProcessVariables().get(VariableConstant.DATA);
        //内部外部发起判断
        String outSider = String.valueOf(historicProcessInstance.getProcessVariables().get(VariableConstant.OutSider));
        TaskInstanceDetailDTO taskInstanceDetailDTO = new TaskInstanceDetailDTO();
        ExtProcessInstancePO extProcessInstancePO = extProcessInstanceMapper.selectById(processInstanceId);
        if (Objects.nonNull(endTime)) {
            // 已完成 获取流程数据
            // 获取processJson
            Properties properties = new Properties();
            properties.put(VariableConstant.FORM_INSTANCE_Id, historicProcessInstance.getProcessVariables()
                    .get(VariableConstant.FORM_INSTANCE_Id));
            FormInstanceDetailVO formInstanceDetailVO = formInstanceQueryService.get(Long.parseLong(properties.getProperty(VariableConstant.FORM_INSTANCE_Id)));
            properties.put(ProcessConstant.FORM_JSON, formInstanceDetailVO.getFormDefinitionVO().getJsonSchema());
            ProcessDefinitionDTO processDefinitionDTO = processDefinitionQueryService.get(historicProcessInstance.getProcessDefinitionKey());
            properties.put(ProcessConstant.PROCESS_JSON, processDefinitionDTO.getProcessFormJson());
            ChildNode childNode = getChildNode(processDefinitionDTO.getProcessFormJson(), historicProcessInstance.getId());
            taskInstanceDetailDTO = TaskInstanceDetailDTO.builder()
                    .processInstanceRunningInfo(new TaskInstanceDetailDTO.ProcessInstanceRunningInfo(
                            historicProcessInstance.getId(),
                            Long.parseLong(historicProcessInstance.getStartUserId()),
                            Objects.nonNull(extProcessInstancePO.getStartUserName()) ? extProcessInstancePO.getStartUserName() : null,
                            Objects.nonNull(status) ? status.toString() : null,
                            StringUtils.equals(outSider, historicProcessInstance.getStartUserId())
                            , this.getRootOperateAlias(userId, Long.parseLong(historicProcessInstance.getStartUserId()), processDefinitionDTO.getProcessFormJson()),
                            Objects.nonNull(childNode.getProps()) ? childNode.getProps().getFormPerms() : null,
                            Objects.nonNull(childNode.getProps()) ? childNode.getProps().getRejectStrategy() : null))
                    .properties(properties)
                    .flowKey(historicProcessInstance.getProcessDefinitionKey())
                    .data(data)
                    .businessType(extProcessInstancePO.getBusinessType())
                    .build();
        } else {
            Task task = getActiveNodeTask(processInstanceId, userId);
            taskInstanceDetailDTO.setBusinessType(Objects.nonNull(extProcessInstancePO) ? extProcessInstancePO.getBusinessType() : null);
            if (ObjectUtils.isNotEmpty(task)) {
                taskInstanceDetailDTO.setId(task.getId());
                taskInstanceDetailDTO.setClaimTime(DateUtils.parse(task.getClaimTime()));
                taskInstanceDetailDTO.setCreateTime(DateUtils.parse(task.getCreateTime()));
                taskInstanceDetailDTO.setKey(task.getTaskDefinitionKey());
                taskInstanceDetailDTO.setData(data);
                taskInstanceDetailDTO.setName(task.getName());
                ProcessDefinitionDTO processDefinitionDTO = processDefinitionQueryService.get(historicProcessInstance.getProcessDefinitionKey());
                Properties properties = new Properties();
                properties.put(VariableConstant.FORM_INSTANCE_Id, historicProcessInstance.getProcessVariables()
                        .get(VariableConstant.FORM_INSTANCE_Id));
                FormInstanceDetailVO formInstanceDetailVO = formInstanceQueryService.get(Long.parseLong(properties.getProperty(VariableConstant.FORM_INSTANCE_Id)));
                properties.put(ProcessConstant.FORM_JSON, formInstanceDetailVO.getFormDefinitionVO().getJsonSchema());
                properties.put(ProcessConstant.PROCESS_JSON, processDefinitionDTO.getProcessFormJson());
                //审批意见 签名
                taskInstanceDetailDTO.setProperties(properties);
                // 获取当前节点的已处理人 思路：获取当前节点的处理过任务的分配人
                List<HistoricTaskInstance> allHistoricTaskInstances = historyService.createHistoricTaskInstanceQuery()
                        .taskDefinitionKey(task.getTaskDefinitionKey())
                        .processInstanceId(task.getProcessInstanceId())
                        .list();
                Set<Long> handled = new HashSet<>();
                //                    任务结束时间是否为空判断任务是否结束
                if (CollectionUtils.isNotEmpty(allHistoricTaskInstances)) {
                    handled.addAll(
                            allHistoricTaskInstances.stream()
                                    .filter(t -> Objects.nonNull(t.getEndTime()))
                                    .map(historicTaskInstance -> Long.parseLong(historicTaskInstance.getAssignee()))
                                    .collect(Collectors.toSet())
                                  );
                    /*
                     * 当前节点，哪些人能操作，哪些人不能操作
                     * 假设当前节点有5个任务：A B C D E
                     * AB已经完成，那么AB不能显示按钮，CDE可以显示
                     * 任务结束时间是否为空判断任务是否结束
                     */
                    // 获取当前节点的剩余处理人
                    Set<Long> allAssignee = new HashSet<>();
                    /*
                     * 获取当前节点的剩余处理人
                     * 思路：获取节点产生的所有任务的分配人+候选人 - 已处理人
                     */
                    allAssignee.addAll(
                            allHistoricTaskInstances.stream()
                                    .filter(t -> Objects.isNull(t.getEndTime()))
                                    .map(historicTaskInstance -> Long.parseLong(historicTaskInstance.getAssignee()))
                                    .collect(Collectors.toList())
                                      );
                    taskInstanceDetailDTO.setHandledOfActive(handled);
                    taskInstanceDetailDTO.setRemainderOfActive(allAssignee);
                }
                ;
                ChildNode childNode = ExtProcessDefinition.getChildNode(JsonUtils.deserialize(processDefinitionDTO.getProcessFormJson(), new TypeReference<ChildNode>() {}),
                                                                        task.getTaskDefinitionKey());
                taskInstanceDetailDTO.setProcessInstanceRunningInfo(
                        new TaskInstanceDetailDTO.ProcessInstanceRunningInfo(
                                historicProcessInstance.getId(),
                                Long.parseLong(historicProcessInstance.getStartUserId()),
                                Objects.nonNull(extProcessInstancePO.getStartUserName()) ? extProcessInstancePO.getStartUserName() : null,
                                status.toString(),
                                StringUtils.equals(outSider, historicProcessInstance.getStartUserId()),
                                getOperateAlias(userId, childNode, taskInstanceDetailDTO,
                                                Long.parseLong(historicProcessInstance.getStartUserId()), processDefinitionDTO.getProcessFormJson(),
                                                historicProcessInstance.getId()
                                               ),
                                null,
                                Objects.nonNull(childNode) ? childNode.getProps().getRejectStrategy() : null
                        )
                                                                   );
                taskInstanceDetailDTO.setFlowKey(historicProcessInstance.getProcessDefinitionKey());
            }
        }

        log.info("<==========获取表单数据getTaskFormById总耗时:{}", System.currentTimeMillis() - startTime);
        return taskInstanceDetailDTO;
    }

    private List<OperateAlias> getRootOperateAlias(Long userId, Long startUserId, String processFormJson) {
        List<OperateAlias> result = new ArrayList<>();
        //不是发起人没有再次发起按钮
        if (!userId.equals(startUserId)) {
            return result;
        }
        ChildNode childNode = ExtProcessDefinition
                .getChildNode(JsonUtils.deserialize(processFormJson, new TypeReference<ChildNode>() {}), "root");
        if (Objects.isNull(childNode)) {
            return result;
        }
        if (Objects.isNull(childNode.getProps())) {
            return result;
        }
        Optional.ofNullable(childNode.getProps().getOperateAlias())
                .orElse(Collections.singletonList(OperateAlias.getReStartAlias()))
                .stream().filter(t -> (StringUtils.isEmpty(t.getPerm()) || "S".equals(t.getPerm()))
                                      && "REPEAT".equals(t.getCode()))
                .forEach(result::add);
        return result;

    }

    private List<OperateAlias> getOperateAlias(Long userId, ChildNode childNode, TaskInstanceDetailDTO taskInstanceDetailDTO,
                                               Long startUserId,
                                               String processFormJson,
                                               String processId
                                              ) {
        List<OperateAlias> result = new ArrayList<>();
        if (Objects.isNull(childNode)) {
            return null;
        }
        if (Objects.isNull(childNode.getProps())) {
            return null;
        }
        //当前活跃节点包含登录人
        if (taskInstanceDetailDTO.getRemainderOfActive().contains(userId)) {
            //别名操作是否为空,为空则取默认值,不为空取配置中显示的值
            Optional.ofNullable(childNode.getProps().getOperateAlias())
                    .orElse(OperateAlias.getActiveOperateAlias())
                    .stream().filter(t -> (StringUtils.isEmpty(t.getPerm()) || "S".equals(t.getPerm()))
                                          && !OperateAlias.getStartAlias().contains(t.getCode())
                                    ).forEach(result::add);
        }

        //不能撤回且有撤回按钮
        if (this.withdrawNodePreCheck(processId, userId, null)) {
            Optional<OperateAlias> withdraw = Optional.ofNullable(childNode.getProps().getOperateAlias())
                    .orElse(OperateAlias.getActiveOperateAlias())
                    .stream().filter(t -> "WITHDRAW".equals(t.getCode())
                                    ).findFirst();
            Optional<OperateAlias> operateAlias = result.stream().filter(t -> "WITHDRAW".equals(t.getCode())).findFirst();
            //没有撤回按钮并且 撤回按钮配置不是隐藏则添加
            if (!operateAlias.isPresent() && withdraw.isPresent() && !"H".equals(withdraw.get().getPerm())) {
                result.add(OperateAlias.getWithdrawAlias());
            }
            //没有撤回按钮并且配置也没有撤回按钮
            if ((!withdraw.isPresent() && !operateAlias.isPresent())) {
                result.add(OperateAlias.getWithdrawAlias());
            }
            if (withdraw.isPresent() && "H".equals(withdraw.get().getPerm())) {
                result = result.stream().filter(t -> !"WITHDRAW".equals(t.getCode())).collect(Collectors.toList());
            }
        } else {
            result = result.stream().filter(t -> !"WITHDRAW".equals(t.getCode())).collect(Collectors.toList());
        }
        //当前用户为发起人并且任务未结束则有撤销按钮
        if (userId.equals(startUserId)) {
            ChildNode rootChildNode = ExtProcessDefinition
                    .getChildNode(JsonUtils.deserialize(processFormJson, new TypeReference<ChildNode>() {}), "root");
            if (Objects.isNull(rootChildNode) || Objects.isNull(rootChildNode.getProps())) {
                return result;
            }
            //跟节点操作别名是否为空
            Optional.ofNullable(rootChildNode.getProps().getOperateAlias())
                    .orElse(Collections.singletonList(OperateAlias.getUndoAlias()))
                    .stream().filter(t -> (StringUtils.isEmpty(t.getPerm()) || "S".equals(t.getPerm()))
                                          && "UNDO".equals(t.getCode()))
                    .forEach(result::add);
            Optional<OperateAlias> operateAlias = Optional.ofNullable(rootChildNode.getProps().getOperateAlias())
                    .orElse(new ArrayList<>())
                    .stream().filter(t -> "UNDO".equals(t.getCode())).findFirst();
            if (!operateAlias.isPresent() && !result.contains(OperateAlias.getUndoAlias())) {
                result.add(OperateAlias.getUndoAlias());
            }

        }
        return result;
    }

    private ChildNode getChildNode(String processFormJson, String proInsId) {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(proInsId)
                .orderByTaskCreateTime().desc().list();
        if (CollectionUtils.isEmpty(historicTaskInstances)) {
            return new ChildNode();
        }
        ChildNode childNode = ExtProcessDefinition.getChildNode(JsonUtils.deserialize(processFormJson, new TypeReference<ChildNode>() {}),
                                                                historicTaskInstances.get(0).getTaskDefinitionKey());
        if (Objects.isNull(childNode)) {
            return new ChildNode();
        }
        if (Objects.isNull(childNode.getProps())) {
            return new ChildNode();
        }
        return childNode;
    }

    /**
     * 获取流程当前活跃节点
     *
     * @param processInstanceId 流程实例id
     * @param userId            任务归属用户
     *
     * @return 节点任务
     */
    private Task getActiveNodeTask(String processInstanceId, Long userId) {
        Task task = null;
        // 未完成
        List<Task> list = taskService.createTaskQuery().taskAssignee(userId.toString()).processInstanceId(processInstanceId).active()
                .list();
        if (ObjectUtils.isNotEmpty(list)) {
            task = list.get(0);
            log.info("按processInstanceId+taskAssignee获取活跃节点任务:{}", task.getId());
        } else {
            List<Task> tasks = taskService.createTaskQuery().active().processInstanceId(processInstanceId).list();
            if (ObjectUtils.isNotEmpty(tasks)) {
                task = tasks.get(0);
                log.info("按processInstanceId获取活跃节点任务:{}", task.getId());
            }
        }
        return task;
    }

    /**
     * 撤回节点前置校验
     * 满足以下情况才能撤回节点：
     * 1、上个节点不是发起节点；2、上个节点审批人为当前用户；
     *
     * @author: lijie
     * @date: 2024/9/3 18:33
     * @param: processInstanceId 流程实例id
     * @param: userId 任务归属用户
     * @return: 是否可以撤回审批节点
     **/
    public Boolean withdrawNodePreCheck(String processInstanceId, Long userId, ProcessWithdrawMessage message) {
        Boolean flag = false;
        Integer size = 2;
        List<HistoricTaskInstance> historicTasks = this.getHistoricTaskInstance(processInstanceId);
        if (ObjectUtils.isNotEmpty(historicTasks) && historicTasks.size() > size) {
            // 获取当前节点
            HistoricTaskInstance afoot = historicTasks.get(historicTasks.size() - 1);
            HistoricTaskInstance before = null;
            for (int i = 0; i < historicTasks.size(); i++) {
                HistoricTaskInstance node = historicTasks.get(i);
                if (node.getTaskDefinitionKey().equals(afoot.getTaskDefinitionKey())) {
                    before = historicTasks.get(i - 1);
                    break;
                }
            }
            // 判断是否是发起节点
            boolean isRoot = ROOT.equals(afoot.getTaskDefinitionKey());
            // 判断上个节点是否是当前用户
            boolean assignee = ObjectUtils.isNotEmpty(before) && before.getAssignee().equals(String.valueOf(userId));
            if (!isRoot && assignee) {
                // 判断是否有抄送
                long count = copyLogMapper.selectCount(new LambdaQueryWrapper<CopyLogPO>()
                                                               .eq(CopyLogPO::getProcessInstanceId, processInstanceId)
                                                               .ge(CopyLogPO::getCreateTime, before.getStartTime()));
                flag = count < 1;
                if (flag) {
                    // 判断上个节点是否是会签、或签
                    Map<String, Integer> nodeMap = this.getNodeCount(historicTasks);
                    flag = nodeMap.get(afoot.getTaskDefinitionKey()) == 1 && nodeMap.get(before.getTaskDefinitionKey()) == 1;
                }
            }
            //当前为整个流程的第几个节点
            int index = historicTasks.indexOf(afoot) + 1;
            if (Objects.nonNull(message)) {
                message.setProcessNodeNum(index);
                message.setProcessInstanceId(processInstanceId);
            }
        }
        return flag;
    }

    /**
     * @description: 获取所有节点
     * @author: lijie
     * @date: 2024/9/5 14:46
     * @param: processInstanceId 流程实例id
     * @return: 流程节点
     **/
    private List<HistoricTaskInstance> getHistoricTaskInstance(String processInstanceId) {
        List<HistoricTaskInstance> nodes = new ArrayList<>();
        HistoricTaskInstanceQuery historicTaskQuery = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId);
        List<HistoricTaskInstance> historicTasks = historicTaskQuery.list();
        // 根据节点id过滤重复数据
        if (ObjectUtils.isEmpty(historicTasks)) {
            return historicTasks;
        }
        Map<String, HistoricTaskInstance> afoot = new HashMap<>();
        for (HistoricTaskInstance historicTask : historicTasks) {
            if (ObjectUtils.isEmpty(historicTask.getEndTime())) {
                nodes.add(historicTask);
                afoot.put(historicTask.getTaskDefinitionKey(), historicTask);
            }
        }
        // 根据时间进行排序
        historicTasks.sort(Comparator.comparing(HistoricTaskInstance::getStartTime));
        for (HistoricTaskInstance historicTask : historicTasks) {
            if (ObjectUtils.isNotEmpty(afoot.get(historicTask.getTaskDefinitionKey()))) {
                break;
            }
            nodes.add(historicTask);
        }
        nodes.sort(Comparator.comparing(HistoricTaskInstance::getStartTime));
        return nodes;
    }

    /**
     * @description: 节点任务数
     * @author: lijie
     * @date: 2024/9/11 14:51
     * @param:
     * @return:
     **/
    private Map<String, Integer> getNodeCount(List<HistoricTaskInstance> historicTasks) {
        // 节点任务数
        Map<String, Integer> nodeMap = new HashMap<>(10);
        for (HistoricTaskInstance historicTask : historicTasks) {
            if (nodeMap.containsKey(historicTask.getTaskDefinitionKey())) {
                int count = nodeMap.get(historicTask.getTaskDefinitionKey());
                count++;
                nodeMap.put(historicTask.getTaskDefinitionKey(), count);
            } else {
                nodeMap.put(historicTask.getTaskDefinitionKey(), 1);
            }
        }
        return nodeMap;
    }
}