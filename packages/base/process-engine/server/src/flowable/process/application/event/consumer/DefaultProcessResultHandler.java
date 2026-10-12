package com.guhecloud.cube.base.flow.flowable.process.application.event.consumer;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.ProcessInstanceCommandService;
import com.guhecloud.cube.base.flow.flowable.process.domain.ProcessInstanceContext;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.result.ProcessResultHandler;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/10/15 11:20
 * @description 默认监听流程所有结果
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultProcessResultHandler extends ProcessResultHandler {
    private final ProcessInstanceCommandService processInstanceCommandService;
    private final TaskService taskService;

    @Override
    public List<String> interest() {
        return Collections.emptyList();
    }

    @Override
    public void onEvent(ProcessInstanceContext processInstanceContext) {

        String processInstanceId = processInstanceContext.getExtProcessInstance().getProcessInstance().getId();
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        list = list.stream().filter(t -> !VariableConstant.ROOT.equals(t.getTaskDefinitionKey()))
                .collect(Collectors.toList());
        if (TaskOperationType.PASS.name().equals(processInstanceContext.getResult()) || CollectionUtils.isEmpty(list)) {
            processInstanceCommandService.notifyFlowStartUser(processInstanceId);
            processInstanceCommandService.endProcessInstance(TaskOperationType.PASS, processInstanceId, processInstanceContext.getResult());
        }
    }
}
