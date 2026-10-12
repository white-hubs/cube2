package com.guhecloud.cube.base.flow.flowable.definition.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;
import com.guhecloud.cube.base.flow.flowable.definition.domain.repository.ProcessDefinitionRepository;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.mapper.ProcessDefinitionMapper;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po.ProcessDefinitionPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pengjian
 * @date 2022/8/30 16:17
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultProcessDefinitionRepository implements ProcessDefinitionRepository {
    private final ProcessDefinitionMapper processDefinitionMapper;
    private final RepositoryService repositoryService;

    @Override
    public void save(ExtProcessDefinition extProcessDefinition) {
        ProcessDefinitionPO processDefinitionPO = ObjectUtils.copying(extProcessDefinition, ProcessDefinitionPO.class);
        Long count = processDefinitionMapper.selectCount(
                Wrappers.<ProcessDefinitionPO>lambdaQuery()
                        .eq(ProcessDefinitionPO::getKey, extProcessDefinition.getKey())
                                                        );
        if (count > 0) {
            processDefinitionMapper.delete(
                    Wrappers.<ProcessDefinitionPO>lambdaQuery()
                            .eq(ProcessDefinitionPO::getKey, extProcessDefinition.getKey())
                                          );
        }
        processDefinitionMapper.insert(processDefinitionPO);
    }

    @Override
    public ExtProcessDefinition getById(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
        return get(processDefinition);
    }

    @Override
    public ExtProcessDefinition getByKey(String processDefinitionKey) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey)
                .singleResult();
        return get(processDefinition);
    }

    private ExtProcessDefinition get(ProcessDefinition processDefinition) {
        Process mainProcess = repositoryService.getBpmnModel(processDefinition.getId()).getMainProcess();
        return new ExtProcessDefinition(
                processDefinition.getKey(),
                null,
                mainProcess,
                processDefinition
        );
    }
}
