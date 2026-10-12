package com.guhecloud.cube.base.flow.flowable.common.resovler;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 上一节点审批人解析器。
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class PreviousAssigneeResolver {

    private static final int HISTORY_PAGE_SIZE = 10;

    private final RepositoryService repositoryService;
    private final HistoryService historyService;

    public List<String> resolve(String activityId, DelegateExecution execution) {
        if (ProcessConstant.PRE_SHOW.equals(execution.getProcessInstanceId())) {
            return Collections.emptyList();
        }

        // NodeUserParser may receive an ExecutionEntity loaded outside an Activiti command context.
        // Calling execution.getCurrentFlowElement() in that case can trigger lazy loading and fail,
        // so resolve the node from the deployed BPMN model using the explicit activity id instead.
        FlowElement currentFlowElement = repositoryService.getBpmnModel(execution.getProcessDefinitionId())
                .getFlowElement(activityId);
        if (!(currentFlowElement instanceof FlowNode)) {
            log.warn("查询上一节点审批人失败，当前节点不是流程节点，节点id: {}", activityId);
            return Collections.emptyList();
        }

        Set<String> previousTaskKeys = findPreviousUserTaskKeys((FlowNode) currentFlowElement, activityId);
        HistoricTaskInstance latestTask = null;
        for (String previousTaskKey : previousTaskKeys) {
            HistoricTaskInstance task = findLatestFinishedTask(execution.getProcessInstanceId(), previousTaskKey);
            if (task != null && (latestTask == null || task.getEndTime().after(latestTask.getEndTime()))) {
                latestTask = task;
            }
        }

        if (latestTask == null) {
            log.info("未查询到上一节点审批人，当前节点id: {}, 前驱节点: {}", activityId, previousTaskKeys);
            return Collections.emptyList();
        }
        return Collections.singletonList(latestTask.getAssignee());
    }

    /**
     * 沿入线向前查找每条路径上最近的用户任务，网关、服务任务等非用户节点会被跳过。
     */
    private Set<String> findPreviousUserTaskKeys(FlowNode currentFlowNode, String currentActivityId) {
        Set<String> previousTaskKeys = new LinkedHashSet<>();
        Set<String> visitedNodeIds = new HashSet<>();
        Deque<FlowNode> pendingNodes = new ArrayDeque<>();
        pendingNodes.push(currentFlowNode);

        while (!pendingNodes.isEmpty()) {
            FlowNode flowNode = pendingNodes.pop();
            if (!visitedNodeIds.add(flowNode.getId())) {
                continue;
            }
            for (SequenceFlow incomingFlow : flowNode.getIncomingFlows()) {
                FlowElement sourceFlowElement = incomingFlow.getSourceFlowElement();
                if (sourceFlowElement instanceof UserTask) {
                    String previousTaskKey = sourceFlowElement.getId();
                    if (!VariableConstant.ROOT.equals(previousTaskKey) && !currentActivityId.equals(previousTaskKey)) {
                        previousTaskKeys.add(previousTaskKey);
                    }
                } else if (sourceFlowElement instanceof FlowNode) {
                    pendingNodes.push((FlowNode) sourceFlowElement);
                }
            }
        }
        return previousTaskKeys;
    }

    private HistoricTaskInstance findLatestFinishedTask(String processInstanceId, String taskDefinitionKey) {
        int firstResult = 0;
        while (true) {
            // The next-node start listener runs before Activiti flushes the completed task's END_TIME_ to the database.
            // Query without finished() so DbSqlSession can replace the row with the ended entity from its command cache.
            List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .taskDefinitionKey(taskDefinitionKey)
                    .orderByHistoricTaskInstanceStartTime()
                    .desc()
                    .listPage(firstResult, HISTORY_PAGE_SIZE);
            for (HistoricTaskInstance task : tasks) {
                if (task.getEndTime() != null && StringUtils.isNotBlank(task.getAssignee()) &&
                    StringUtils.isBlank(task.getDeleteReason())) {
                    return task;
                }
            }
            if (tasks.size() < HISTORY_PAGE_SIZE) {
                return null;
            }
            firstResult += HISTORY_PAGE_SIZE;
        }
    }
}
