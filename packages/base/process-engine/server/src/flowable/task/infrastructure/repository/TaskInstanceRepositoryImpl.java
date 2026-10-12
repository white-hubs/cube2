package com.guhecloud.cube.base.flow.flowable.task.infrastructure.repository;

import base.constant.VariableConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.flowable.task.domain.TaskInstance;
import com.guhecloud.cube.base.flow.flowable.task.domain.repository.TaskInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskRuNodeMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskRuNodePO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/9/5 21:17
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskInstanceRepositoryImpl implements TaskInstanceRepository {
    private final TaskService taskService;
    private final HistoryService historyService;
    private final TaskRuNodeMapper taskRuNodeMapper;

    @Override
    public Optional<TaskInstance> get(String taskId) {

        Boolean isActived;
        String data;
        // 获取task
        Task task = taskService.createTaskQuery().includeProcessVariables().includeTaskLocalVariables().taskId(taskId).singleResult();
        TaskInfo taskInfo = null;
        if (Objects.isNull(task)) {
            // 查询历史任务
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().includeProcessVariables()
                    .includeTaskLocalVariables().taskId(taskId).singleResult();
            if (Objects.isNull(historicTaskInstance)) {
                return Optional.empty();
            } else {
                taskInfo = historicTaskInstance;
                isActived = false;
                HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                        .taskId(taskId).variableName(VariableConstant.DATA).singleResult();
                data = (String) historicVariableInstance.getValue();
            }
        } else {
            taskInfo = task;
            isActived = true;
            data = (String) taskService.getVariable(taskId, VariableConstant.DATA);
        }

        TaskRuNodePO taskRuNodePO = taskRuNodeMapper.selectOne(
                Wrappers.<TaskRuNodePO>lambdaQuery()
                        .eq(TaskRuNodePO::getTaskKey, taskInfo.getTaskDefinitionKey())
                        .eq(TaskRuNodePO::getProcessInstanceId, taskInfo.getProcessInstanceId())
                                                              );
        if (Objects.isNull(taskRuNodePO)) {
            return Optional.empty();
        }

        TaskInstance taskInstance = ObjectUtils.copying(taskRuNodePO, TaskInstance.class);
        taskInstance.setTaskInfo(taskInfo);
        taskInstance.setActived(isActived);
        taskInstance.setData(data);
        return Optional.of(taskInstance);
    }

}
