package com.guhecloud.cube.base.flow.flowable.process.domain.respository;

import com.guhecloud.cube.base.flow.flowable.process.domain.ExtHistoricProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/9/5 16:43
 * @description
 */
public interface ExtProcessInstanceRepository {

    void save(ExtProcessInstance extProcessInstance);

    Optional<ExtProcessInstance> get(String id);

    Optional<ExtHistoricProcessInstance> getHistoric(String id);

    Optional<ExtProcessInstance> getByBusinessKey(String businessKey);

    boolean setStartUserName(ExtProcessInstancePO processInstancePO);

    Map<String, ExtProcessInstance> getBatch(Collection<String> processInstanceIds);
}
