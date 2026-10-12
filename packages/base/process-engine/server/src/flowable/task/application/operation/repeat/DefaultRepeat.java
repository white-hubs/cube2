package com.guhecloud.cube.base.flow.flowable.task.application.operation.repeat;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.context.submit.commandgateway.SubmitFormFlowCommandService;
import com.guhecloud.cube.base.flow.context.submit.commandgateway.command.SubmitAgainFormCommand;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtHistoricProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.AbstractTaskOperation;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Scope("prototype")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultRepeat extends AbstractTaskOperation<RepeatCommand> implements Repeat {
    private final HistoryService historyService;
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final ExtProcessInstanceMapper extProcessInstanceMapper;
    private final SubmitFormFlowCommandService submitFormFlowCommandService;

    @Override
    public void execute(RepeatCommand repeatCommand) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(repeatCommand.getProcessInstanceId()).singleResult();
        Optional<ExtHistoricProcessInstance> optional = extProcessInstanceRepository.getHistoric(historicProcessInstance.getId());
        optional.ifPresent(extProcessInstance -> {
            SubmitAgainFormCommand submitAgainFormCommand = packagingCommand(extProcessInstance);
            submitFormFlowCommandService.submitAgain(submitAgainFormCommand);
        });
    }

    private SubmitAgainFormCommand packagingCommand(ExtHistoricProcessInstance extProcessInstance) {
        Map<String, Object> variables = packagingContext(extProcessInstance);
        SubmitAgainFormCommand submitAgainFormCommand = new SubmitAgainFormCommand();
        submitAgainFormCommand.setData(variables.get(VariableConstant.DATA).toString());
        submitAgainFormCommand.setFormInstanceId(Long.parseLong(variables.get(VariableConstant.FORM_INSTANCE_Id).toString()));
        submitAgainFormCommand.setVariables(variables);
        String procInstanceId = extProcessInstance.getProcessInstance().getId();
        ExtProcessInstancePO instancePO = extProcessInstanceMapper.selectById(procInstanceId);
        submitAgainFormCommand.setBackLogTitle(instancePO.getInstanceName());
        return submitAgainFormCommand;
    }

    private Map<String, Object> packagingContext(ExtHistoricProcessInstance extProcessInstance) {
        List<HistoricVariableInstance> formInstanceId = historyService.createHistoricVariableInstanceQuery()
                .variableName(VariableConstant.FORM_INSTANCE_Id).processInstanceId(extProcessInstance.getProcessInstance().getId()).list();
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().variableName(VariableConstant.DATA)
                .processInstanceId(extProcessInstance.getProcessInstance().getId()).list();
        Map<String, Object> variables = new HashMap<>();
        variables.put(VariableConstant.FORM_INSTANCE_Id, formInstanceId.get(0).getValue());
        variables.put(VariableConstant.DATA, list.get(0).getValue());
        variables.putAll(extProcessInstance.getProcessInstance().getProcessVariables());
        variables.remove(VariableConstant.RESULT);
        return variables;
    }
}
