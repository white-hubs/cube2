package com.guhecloud.cube.base.flow.flowable.definition.domain.repository;

import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;

/**
 * @author pengjian
 * @date 2022/8/30 16:18
 * @description
 */
public interface ProcessDefinitionRepository {

    void save(ExtProcessDefinition extProcessDefinition);

    ExtProcessDefinition getById(String processDefinitionId);

    ExtProcessDefinition getByKey(String processDefinitionKey);
}
