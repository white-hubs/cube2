package com.guhecloud.cube.base.flow.flowable.task.application.operation.preReduceSign;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.AbstractTaskOperation;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultPreReduceSign extends AbstractTaskOperation<PreReduceSignCommand> implements PreReduceSign {

    private final RuntimeService runtimeService;

    @Override
    public void execute(PreReduceSignCommand preReduceSignCommand) {
        // 只能减临时加的，不能减流程原有配置的人员
        String variable = VariableConstant.TEMPORARY + preReduceSignCommand.getTaskKey() + VariableConstant.ASSIGNEE_List;
        List<String> assigneeList = (List<String>) runtimeService.getVariable(preReduceSignCommand.getProcessInstanceId(), variable);
        assigneeList.removeAll(preReduceSignCommand.getUserIds().stream().map(Object::toString).collect(Collectors.toSet()));
        runtimeService.setVariable(preReduceSignCommand.getProcessInstanceId(), variable, assigneeList);
    }
}
