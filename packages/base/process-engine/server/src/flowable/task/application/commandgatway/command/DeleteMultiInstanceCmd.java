package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量删除未完成的并行多实例任务，并同步维护多实例计数。
 */
public class DeleteMultiInstanceCmd implements Command<Void> {

    private static final String NUMBER_OF_INSTANCES = "nrOfInstances";
    private static final String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";
    private static final String DELETE_REASON = "node transfer";

    private final List<String> taskIds;

    public DeleteMultiInstanceCmd(List<String> taskIds) {
        this.taskIds = taskIds;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        if (taskIds == null || taskIds.isEmpty()) {
            return null;
        }

        List<ExecutionEntity> executionEntities = new ArrayList<>();
        ExecutionEntity parentExecutionEntity = null;
        for (String taskId : taskIds) {
            TaskEntity taskEntity = taskEntityManager.findById(taskId);
            if (taskEntity == null) {
                throw new ActivitiException("待删除的多实例任务不存在: " + taskId);
            }
            ExecutionEntity executionEntity = executionEntityManager.findById(taskEntity.getExecutionId());
            ExecutionEntity parent = executionEntity == null ? null : executionEntity.getParent();
            if (parent == null || !parent.isMultiInstanceRoot()) {
                throw new ActivitiException("此任务不属于多实例节点: " + taskId);
            }
            if (parentExecutionEntity != null && !parentExecutionEntity.getId().equals(parent.getId())) {
                throw new ActivitiException("待删除任务不属于同一个多实例节点");
            }
            parentExecutionEntity = parent;
            executionEntities.add(executionEntity);
        }

        int nrOfInstances = getCounter(parentExecutionEntity, NUMBER_OF_INSTANCES);
        int nrOfActiveInstances = getCounter(parentExecutionEntity, NUMBER_OF_ACTIVE_INSTANCES);
        if (nrOfInstances <= taskIds.size() || nrOfActiveInstances <= taskIds.size()) {
            throw new ActivitiException("多实例节点至少需要保留一个活动任务");
        }

        parentExecutionEntity.setVariableLocal(NUMBER_OF_INSTANCES, nrOfInstances - taskIds.size());
        parentExecutionEntity.setVariableLocal(NUMBER_OF_ACTIVE_INSTANCES, nrOfActiveInstances - taskIds.size());
        executionEntities.forEach(executionEntity ->
                executionEntityManager.cancelExecutionAndRelatedData(executionEntity, DELETE_REASON));
        return null;
    }

    private int getCounter(ExecutionEntity executionEntity, String variableName) {
        Object value = executionEntity.getVariableLocal(variableName);
        if (!(value instanceof Number)) {
            throw new ActivitiException("多实例计数变量不存在: " + variableName);
        }
        return ((Number) value).intValue();
    }
}
