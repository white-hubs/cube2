package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.history.HistoryManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntityManager;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import com.guhecloud.platform.base.util.StringUtils;

public class JumpCommand implements Command<Void> {

    private final String taskId;
    private final String targetNodeId;
    private final String reason;
    private final String userTaskAssignee;

    public JumpCommand(String taskId, String targetNodeId, String reason, String userTaskAssignee) {
        this.taskId = taskId;
        this.targetNodeId = targetNodeId;
        this.reason = reason;
        this.userTaskAssignee = userTaskAssignee;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        // 服务类
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        HistoryManager historyManager = commandContext.getHistoryManager();
        VariableInstanceEntityManager variableInstanceEntityManager = commandContext.getVariableInstanceEntityManager();
        ActivitiEngineAgenda agenda = commandContext.getAgenda();
        // 任务对象
        TaskEntity taskEntity = taskEntityManager.findById(taskId);
        // 流程定义对象
        BpmnModel bpmnModel = ProcessDefinitionUtil.getBpmnModel(taskEntity.getProcessDefinitionId());
        // 当前任务节点对象，封装了节点的元数据，如入线和出线
        Activity taskNode = (Activity) bpmnModel.getFlowElement(taskEntity.getTaskDefinitionKey());
        // 目标任务节点
        Activity targetNode = (Activity) bpmnModel.getFlowElement(targetNodeId);
        if (targetNode == null) {
            throw new RuntimeException("目标节点不存在");
        }
        // 执行对象
        ExecutionEntity executionEntity = executionEntityManager.findById(taskEntity.getExecutionId());
        if (taskNode.getBehavior() instanceof MultiInstanceActivityBehavior) {
            // 多实例节点，找出其父级执行对象
            executionEntity = executionEntity.getParent();
            // 删除下级执行对象
            executionEntityManager.deleteChildExecutions(executionEntity, reason);
            // 更新父级执行对象
            executionEntity.setActive(true);
            executionEntity.setMultiInstanceRoot(false);
            executionEntityManager.update(executionEntity);
        } else {
            // 单实例节点，删除任务
            taskEntityManager.delete(taskId);
            // 记录任务结束历史
            historyManager.recordTaskEnd(taskId, reason);
            // 记录任务结束历史之后（变量进入历史表），删除任务变量（因为任务是直接删除不是complete，会有变量留存在ACT_RU_VARIABLE表中，必须手动删除）
            variableInstanceEntityManager.findVariableInstancesByTaskId(taskId).forEach(variableInstance -> {
                variableInstanceEntityManager.delete(variableInstance, true);
            });
            // 记录活动结束历史，必须在executionEntity.setCurrentFlowElement()之前，否则会因为无法找到正确的FlowElement而导致历史活动不更新
            historyManager.recordActivityEnd(executionEntity, reason);
        }
        // 设置当前节点
        executionEntity.setCurrentFlowElement(targetNode);
        if (StringUtils.isNotBlank(userTaskAssignee)) {
            executionEntity.setVariable("assigneeName", userTaskAssignee);
            executionEntityManager.update(executionEntity);
        }
        // 驱动实例的继续运转，即跳入到目标节点
        if (targetNode.getBehavior() instanceof MultiInstanceActivityBehavior) {
            // 目标节点为多实例
            agenda.planContinueMultiInstanceOperation(executionEntity);
        } else {
            // 目标节点为单实例
            agenda.planContinueProcessInCompensation(executionEntity);
        }
        return null;
    }

}
