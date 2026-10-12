package com.guhecloud.cube.base.flow.flowable.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.common.resovler.PrevUserTaskDTO;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessHelper {

    private final ObjectMapper objectMapper;

    private final RepositoryService repositoryService;

    private final TaskService taskService;

    private final HistoryService historyService;

    private final UserClient userClient;

    private final ProcessEngine processEngine;
    private final ExtProcessInstanceMapper extProcessInstanceMapper;

    /**
     * 检查当前任务有效性
     */
    public Task checkTask(String taskId) {
        Task task = taskService.createTaskQuery().includeTaskLocalVariables().taskId(taskId).singleResult();
        if (task == null) {
            throw new ServiceException("任务不存在或已完成");
        }
        return task;
    }

    public Task checkUserTask(String userId, String taskId) {
        checkTask(taskId);
        Task task = taskService.createTaskQuery().taskCandidateUser(userId).taskId(taskId).singleResult();
        if (Objects.isNull(task)) {
            Task singleResult = taskService.createTaskQuery().taskAssignee(userId).taskId(taskId).singleResult();
            if (Objects.isNull(singleResult)) {
                throw new ServiceException("当前用户不能处理该任务");
            }
            return singleResult;
        }
        return task;
    }

    /**
     * 获取流程实例当前任务
     */
    public List<UserTaskSimpleDTO> getCurrentUserTasksByProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().asc().list().stream()
                .map(task -> {
                    UserTaskSimpleDTO dto = new UserTaskSimpleDTO();
                    dto.setId(task.getId());
                    dto.setKey(task.getTaskDefinitionKey());
                    dto.setName(task.getName());
                    dto.setAssigneeUser(this.getTaskAssigneeUser(task.getAssignee()));
                    dto.setCandidateUsers(this.getTaskCandidateUsers(task.getId()));
                    dto.setClaimTime(DateUtils.parse(task.getClaimTime()));
                    dto.setCreateTime(DateUtils.parse(task.getCreateTime()));
                    String description = task.getDescription();
                    try {
                        TaskAttribute attr = objectMapper.readValue(description, TaskAttribute.class);
                        dto.setAttr(attr);
                    } catch (Exception e) {
                    }
                    dto.setOutcomes(this.getOutcomesOfUserTask(task.getId()));
                    return dto;
                }).collect(Collectors.toList());
    }

    /**
     * 判断任务节点是否为会签
     *
     * @param taskId
     *
     * @return
     */
    public boolean isMultiInstance(String taskId) {
        boolean result = false;
        // 任务对象
        Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            // 流程定义对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            // 当前任务节点对象，封装了节点的元数据，如入线和出线
            Activity taskNode = (Activity) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
            if (taskNode.getBehavior() instanceof MultiInstanceActivityBehavior) {
                result = true;
            }
        }
        return result;
    }

    public boolean isMultiInstance(String taskDefinitionKey, String processDefinitionId) {
        boolean result = false;
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 当前任务节点对象，封装了节点的元数据，如入线和出线
        Activity taskNode = (Activity) bpmnModel.getFlowElement(taskDefinitionKey);
        if (taskNode.getBehavior() instanceof MultiInstanceActivityBehavior) {
            result = true;
        }
        return result;
    }

    /**
     * 获取流程发起人
     */
    public ProcessUserDTO getProcessStartUser(String processInstanceId) {
        UserDTO user;
        if (!ProcessConstant.PRE_SHOW.equals(processInstanceId)) {

            user = userClient.getUser(Long.valueOf(historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId)
                                                           .singleResult().getStartUserId()));
            if (Objects.isNull(user)) {
                ExtProcessInstancePO extProcessInstancePO = extProcessInstanceMapper.selectById(processInstanceId);
                user = new UserDTO();
                user.setId(extProcessInstancePO.getStartUserId());
                user.setUsername(extProcessInstancePO.getStartUserName());
                user.setName(extProcessInstancePO.getStartUserName());
            }
        } else {
            Long startUserId = SecurityContextHolder.getPrincipal().getId();
            user = userClient.getUser(startUserId);
        }
        return user == null ? null : new ProcessUserDTO(String.valueOf(user.getId()), user.getUsername(), user.getName());
    }

    /**
     * 获取任务处理人
     */
    public ProcessUserDTO getTaskAssigneeUser(String assignee) {
        if (StringUtils.isNotEmpty(assignee)) {
            UserDTO user = userClient.getUser(Long.valueOf(assignee));
            if (user != null) {
                return new ProcessUserDTO(String.valueOf(user.getId()), user.getUsername(), user.getName());
            }
        }
        return null;
    }

    /**
     * 获取任务候选人
     */
    public List<ProcessUserDTO> getTaskCandidateUsers(String taskId) {
        List<ProcessUserDTO> taskCandidateUsers = new ArrayList<>();
        if (StringUtils.isNotEmpty(taskId)) {
            if (taskService.createTaskQuery().taskId(taskId).singleResult() != null) {
                for (IdentityLink identityLink : taskService.getIdentityLinksForTask(taskId)) {
                    if (identityLink.getType().equals("candidate") && StringUtils.isNotEmpty(identityLink.getUserId())) {
                        UserDTO user = userClient.getUser(Long.valueOf(identityLink.getUserId()));
                        if (user != null) {
                            taskCandidateUsers.add(new ProcessUserDTO(String.valueOf(user.getId()), user.getUsername(), user.getName()));
                        }
                    }
                }
            } else {
                for (HistoricIdentityLink identityLink : historyService.getHistoricIdentityLinksForTask(taskId)) {
                    if (identityLink.getType().equals("candidate") && StringUtils.isNotEmpty(identityLink.getUserId())) {
                        UserDTO user = userClient.getUser(Long.valueOf(identityLink.getUserId()));
                        if (user != null) {
                            taskCandidateUsers.add(new ProcessUserDTO(String.valueOf(user.getId()), user.getUsername(), user.getName()));
                        }
                    }
                }
            }
        }
        return taskCandidateUsers;
    }

    /**
     * 获取可以回退的UserTask列表（TODO 跨过非用户任务节点）
     */
    public List<PrevUserTaskDTO> determinePrevUserTasks(String userTaskKey, String processDefinitionId) {
        List<PrevUserTaskDTO> prevUserTasks = new ArrayList<>();
        // 获取流程所有节点（StartEvent、EndEvent、UserTask、SequenceFlow）
        Map<String, FlowElement> flowElements = repositoryService.getBpmnModel(processDefinitionId).getMainProcess().getFlowElements()
                .stream().collect(Collectors.toMap(FlowElement::getId, Function.identity()));
        // 从所有流程节点中找出当前任务节点之前的UserTask节点
        List<String> prevUserTaskKeys = new ArrayList<>();
        findPrevUserTaskKeys(flowElements, userTaskKey, prevUserTaskKeys);
        prevUserTaskKeys.forEach(key -> {
            prevUserTasks.add(new PrevUserTaskDTO(key, flowElements.get(key).getName()));
        });
        return prevUserTasks;
    }

    private void findPrevUserTaskKeys(Map<String, FlowElement> flowElements, String userTaskKey, List<String> prevUserTaskKeys) {
        for (FlowElement flowElement : flowElements.values()) {
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                FlowElement sourceFlowElement = sequenceFlow.getSourceFlowElement();
                FlowElement tartgetFlowElement = sequenceFlow.getTargetFlowElement();
                if (sourceFlowElement instanceof UserTask && tartgetFlowElement instanceof UserTask) {
                    UserTask sourceUserTask = (UserTask) sourceFlowElement;
                    UserTask targetUserTask = (UserTask) tartgetFlowElement;
                    if (targetUserTask.getId().equals(userTaskKey)) {
                        if (!prevUserTaskKeys.contains(sourceUserTask.getId())) {
                            prevUserTaskKeys.add(sourceUserTask.getId());
                            findPrevUserTaskKeys(flowElements, sourceUserTask.getId(), prevUserTaskKeys);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取历史任务变量（变量名需要去前缀，仅用于获取传入的自定义本地变量）
     */
    public Map<String, Object> getHistoricTaskLocalVariables(String taskId) {
        Map<String, Object> variables = new HashMap<>();
        if (!StringUtils.isEmpty(taskId)) {
            for (HistoricVariableInstance hvi : historyService.createHistoricVariableInstanceQuery().taskId(taskId).list()) {
                // 必须是匹配当前任务和带特定前缀的变量
                if (taskId.equals(hvi.getTaskId()) && hvi.getVariableName().startsWith(ProcessService.LOCAL_VAR_PREFIX)) {
                    // 排除掉内置的变量
                    if (!(ProcessService.VAR_TASK_COMMENT.equals(hvi.getVariableName()) ||
                          ProcessService.VAR_TASK_RESULT.equals(hvi.getVariableName()))) {
                        // 去掉存储时附加的特定前缀
                        variables.put(hvi.getVariableName().substring(1), hvi.getValue());
                    }
                }
            }
        }
        return variables;
    }

    /**
     * 获取历史任务变量（variableName已带前缀，仅用于获取内置的本地变量如 _comment _result）
     */
    public String getHistoricTaskLocalVariable(String taskId, String variableName) {
        if (!StringUtils.isEmpty(taskId) && !StringUtils.isEmpty(variableName)) {
            for (HistoricVariableInstance hvi : historyService.createHistoricVariableInstanceQuery().taskId(taskId).list()) {
                // 必须是匹配当前任务和名称的变量
                if (taskId.equals(hvi.getTaskId()) && hvi.getVariableName().equals(variableName)) {
                    return (String) hvi.getValue();
                }
            }
        }
        return "";
    }

    /**
     * 删除任务本地变量
     */
    public void clearTaskVariablesLocal(String taskId) {
        Set<String> variableNames = taskService.getVariablesLocal(taskId).keySet();
        taskService.removeVariablesLocal(taskId, variableNames);
    }

    /**
     * 保存任务处理结果变量
     */
    public void setResultTaskVariablesLocal(String taskId, String comment, String result) {
        taskService.setVariableLocal(taskId, ProcessService.VAR_TASK_COMMENT, StringUtils.isEmpty(comment) ? "" : comment);
        taskService.setVariableLocal(taskId, ProcessService.VAR_TASK_RESULT, StringUtils.isEmpty(result) ? "" : result);
    }

    public String getXmlOfProcessDefinition(String processDefinitionId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        BpmnXMLConverter converter = new BpmnXMLConverter();
        byte[] bytes = converter.convertToXML(bpmnModel);
        String xmlContenxt = new String(bytes);
        return xmlContenxt;
    }

    public boolean isFirstUserTask(String taskId) {
        Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            // 流程定义对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            // 当前任务节点对象，封装了节点的元数据，如入线和出线
            Activity taskNode = (Activity) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
            for (SequenceFlow flow : taskNode.getIncomingFlows()) {
                if (flow.getSourceFlowElement() instanceof StartEvent) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getOutcomesOfUserTask(String taskId) {
        List<String> outcomes = new ArrayList<>();
        Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            // 流程定义对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            // 当前任务节点对象，封装了节点的元数据，如入线和出线
            Activity taskNode = (Activity) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
            outcomes.addAll(taskNode.getOutgoingFlows().stream().map(f -> f.getName()).collect(Collectors.toList()));
        }
        return outcomes;
    }

}
