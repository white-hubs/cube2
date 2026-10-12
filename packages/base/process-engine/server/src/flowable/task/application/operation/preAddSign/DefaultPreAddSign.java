package com.guhecloud.cube.base.flow.flowable.task.application.operation.preAddSign;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.AbstractTaskOperation;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RuntimeService;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultPreAddSign extends AbstractTaskOperation<PreAddSignCommand> implements PreAddSign {

    private final RuntimeService runtimeService;

    @Override
    public void execute(PreAddSignCommand passCommand) {

        String variable = passCommand.getTaskKey() + VariableConstant.ASSIGNEE_List;
        List<String> assigneeList = (List<String>) runtimeService.getVariable(passCommand.getProcessInstanceId(), variable);
        List<String> users = passCommand.getUserIds().stream().map(Object::toString).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(assigneeList)) {
            runtimeService.setVariable(passCommand.getProcessInstanceId(), VariableConstant.TEMPORARY + variable, users);
        }
    }
}
