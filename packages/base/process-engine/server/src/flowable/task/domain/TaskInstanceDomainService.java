package com.guhecloud.cube.base.flow.flowable.task.domain;

import com.guhecloud.platform.base.context.ApplicationContextHolder;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/9/5 17:30
 * @description
 */
public class TaskInstanceDomainService {

    /**
     * 判断当前任务是否是第一个任务
     *
     * @param execution
     *
     * @return
     */
    public static boolean isFirstTask(ExecutionEntity execution) {
        FlowElement currentFlowElement = execution.getCurrentFlowElement();
        UserTask currentFlowElementTask = (UserTask) currentFlowElement;
        return isFirstTask(currentFlowElementTask);
    }

    /**
     * 判断上一个节点是否是第一个人工用户任务
     *
     * @param task 当前任务
     *
     * @return
     */
    public static boolean isFirstArtificialTask(Task task) {
        HistoryService historyService = ApplicationContextHolder.getBean(HistoryService.class);
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId())
                .orderByTaskCreateTime().asc().finished().list();
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        if (list.size() == 1) {
            return true;
        }
        // 排除发起节点
        HistoricTaskInstance historicTaskInstance = list.get(1);
        return Objects.nonNull(historicTaskInstance) && historicTaskInstance.getTaskDefinitionKey().equals(task.getTaskDefinitionKey());
    }

    public static boolean isFirstTask(UserTask userTask) {
        List<SequenceFlow> incomingFlows = userTask.getIncomingFlows();
        SequenceFlow sequenceFlow = incomingFlows.get(0);
        FlowElement sourceFlowElement = sequenceFlow.getSourceFlowElement();
        // 判断当前任务的前一个节点是否是StartEvent，即是否是第一个任务
        return sourceFlowElement instanceof StartEvent;
    }

}
