package com.guhecloud.cube.base.flow.flowable.process.application.commandgateway;

import com.guhecloud.cube.base.flow.flowable.process.domain.ExtHistoricProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.ProcessInstanceContext;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessInstanceContextBuilder {
    private final ExtProcessInstanceRepository extProcessInstanceRepository;

    public ProcessInstanceContext build(String processInstanceId) {
        // 封装流程实例上下文
        Optional<ExtHistoricProcessInstance> optional = extProcessInstanceRepository.getHistoric(processInstanceId);
        if (!optional.isPresent()) {
            return new ProcessInstanceContext();
        }
        ExtHistoricProcessInstance extProcessInstance = optional.get();
        return new ProcessInstanceContext(extProcessInstance, extProcessInstance.getResult());
    }
}
