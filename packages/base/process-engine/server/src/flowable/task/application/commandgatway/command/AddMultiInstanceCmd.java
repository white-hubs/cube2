package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import com.guhecloud.platform.base.util.CollectionUtils;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.activiti.engine.runtime.Execution;

import java.util.Objects;

public class AddMultiInstanceCmd implements Command<Execution> {

    protected static final String NUMBER_OF_INSTANCES = "nrOfInstances";
    protected static final String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";
    protected static final String MULTIINSTANCE_ELEMENT_VARIABLE = "assignee";
    protected static final String LOOP_COUNTER = "loopCounter";

    private final String taskId;
    private final String userTaskAssignee;

    public AddMultiInstanceCmd(String taskId, String userTaskAssignee) {
        this.taskId = taskId;
        this.userTaskAssignee = userTaskAssignee;
    }

    @Override
    public Execution execute(CommandContext commandContext) {
        // 服务类
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        // 任务对象
        TaskEntity taskEntity = taskEntityManager.findById(taskId);
        // 根据执行实例ID获取当前执行实例
        ExecutionEntity executionEntity = executionEntityManager.findById(taskEntity.getExecutionId());
        // 获取流程执行实例（即当前执行实例的父实例）
        ExecutionEntity parentExecutionEntity = executionEntity.getParent();
        // 判断当前执行实例的节点是否是多实例节点
        BpmnModel bpmnModel = ProcessDefinitionUtil.getBpmnModel(executionEntity.getProcessDefinitionId());
        Activity activity = (Activity) bpmnModel.getFlowElement(executionEntity.getCurrentActivityId());
        MultiInstanceLoopCharacteristics loopCharacteristics = activity.getLoopCharacteristics();
        if (loopCharacteristics == null) {
            throw new ActivitiException("此节点不是多实例节点");
        }
        if (loopCharacteristics.isSequential()) {
            throw new ActivitiException("此节点为串行节点");
        }
        // 创建新的子实例
        ExecutionEntity childExecution = executionEntityManager.createChildExecution(parentExecutionEntity);
        // 获取并为新的执行实例设置当前活动节点
        UserTask newUserTask = createNewUserTask((UserTask) parentExecutionEntity.getCurrentFlowElement());

        childExecution.setActive(true);
        childExecution.setScope(false);
        childExecution.setCurrentFlowElement(newUserTask);

        // 更新会签计数变量
        Integer nrOfInstances = (Integer) parentExecutionEntity.getVariableLocal(NUMBER_OF_INSTANCES);
        Integer nrOfActiveInstances = (Integer) parentExecutionEntity.getVariableLocal(NUMBER_OF_ACTIVE_INSTANCES);
        parentExecutionEntity.setVariableLocal(NUMBER_OF_INSTANCES, nrOfInstances + 1);
        parentExecutionEntity.setVariableLocal(NUMBER_OF_ACTIVE_INSTANCES, nrOfActiveInstances + 1);

        // 循环次数加1
        childExecution.setVariable("assigneeName", userTaskAssignee);
        childExecution.setVariableLocal(LOOP_COUNTER, nrOfInstances);
        // 为新的执行实例设置处理人
        commandContext.getAgenda().planContinueMultiInstanceOperation(childExecution);

        return childExecution;
    }

    private UserTask createNewUserTask(UserTask flowElement) {
        UserTask userTask = new UserTask();
        userTask.setName(flowElement.getName());
        userTask.setId(flowElement.getId());
        userTask.setAssignee("${assigneeName}");
        if (!CollectionUtils.isEmpty(flowElement.getTaskListeners())) {
            userTask.setTaskListeners(flowElement.getTaskListeners());
        }
        if (!CollectionUtils.isEmpty(flowElement.getExecutionListeners())) {
            userTask.setExecutionListeners(flowElement.getExecutionListeners());
        }
        if (Objects.nonNull(flowElement.getLoopCharacteristics())) {
            userTask.setLoopCharacteristics(flowElement.getLoopCharacteristics());
        }
        userTask.setIncomingFlows(flowElement.getIncomingFlows());
        userTask.setOutgoingFlows(flowElement.getOutgoingFlows());
        userTask.setBehavior(flowElement.getBehavior());
        userTask.setParentContainer(flowElement.getParentContainer());
        userTask.setXmlColumnNumber(flowElement.getXmlColumnNumber());
        userTask.setXmlRowNumber(flowElement.getXmlRowNumber());
        return userTask;
    }

}
