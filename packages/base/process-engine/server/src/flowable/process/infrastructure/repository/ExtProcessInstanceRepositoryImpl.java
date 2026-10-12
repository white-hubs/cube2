package com.guhecloud.cube.base.flow.flowable.process.infrastructure.repository;

import base.constant.VariableConstant;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtHistoricProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/9/5 16:42
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class ExtProcessInstanceRepositoryImpl implements ExtProcessInstanceRepository {

    private final ExtProcessInstanceMapper extProcessInstanceMapper;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final UserClient userClient;

    @Override
    public void save(ExtProcessInstance extProcessInstance) {
        ExtProcessInstancePO processInstancePO = extProcessInstanceMapper.selectById(extProcessInstance.getProcessInstance().getId());
        if (Objects.nonNull(processInstancePO)) {
            processInstancePO.setId(extProcessInstance.getProcessInstance().getId());
            processInstancePO.setApplicationId(extProcessInstance.getApplicationId());
            processInstancePO.setBusinessId(extProcessInstance.getBusinessId());
            processInstancePO.setProcessDefinitionKey(extProcessInstance.getProcessDefinitionKey());
            processInstancePO.setData(JsonUtils.serialize(extProcessInstance.getDataMap()));
            processInstancePO.setApplicationCode(extProcessInstance.getApplicationId().getApplicationCode());
            processInstancePO.setStartUserInfo(extProcessInstance.getStartUserInfo());
            if (ObjectUtils.isNotEmpty(processInstancePO.getStartUserInfo())) {
                processInstancePO.setStartUserId(processInstancePO.getStartUserInfo().getStartUserId());
                processInstancePO.setStartUserName(processInstancePO.getStartUserInfo().getStartUserName());
                processInstancePO.setOrganizationId(processInstancePO.getStartUserInfo().getOrganizationId());
                processInstancePO.setOrganizationName(processInstancePO.getStartUserInfo().getOrganizationName());
            }
            processInstancePO.setInstanceName(extProcessInstance.getInstanceName());
            setStartUserName(processInstancePO);
            extProcessInstanceMapper.updateById(processInstancePO);
        } else {
            ExtProcessInstancePO extProcessInstancePO = new ExtProcessInstancePO();
            extProcessInstancePO.setId(extProcessInstance.getProcessInstance().getId());
            extProcessInstancePO.setApplicationId(extProcessInstance.getApplicationId());
            extProcessInstancePO.setBusinessId(extProcessInstance.getBusinessId());
            extProcessInstancePO.setProcessDefinitionKey(extProcessInstance.getProcessDefinitionKey());
            extProcessInstancePO.setData(JsonUtils.serialize(extProcessInstance.getDataMap()));
            extProcessInstancePO.setApplicationCode(extProcessInstance.getApplicationId().getApplicationCode());
            extProcessInstancePO.setStartUserInfo(extProcessInstance.getStartUserInfo());
            extProcessInstancePO.setInstanceName(extProcessInstance.getInstanceName());
            setStartUserName(extProcessInstancePO);
            if (ObjectUtils.isNotEmpty(extProcessInstancePO.getStartUserInfo())) {
                extProcessInstancePO.setStartUserId(extProcessInstancePO.getStartUserInfo().getStartUserId());
                extProcessInstancePO.setStartUserName(extProcessInstancePO.getStartUserInfo().getStartUserName());
                extProcessInstancePO.setOrganizationId(extProcessInstancePO.getStartUserInfo().getOrganizationId());
                extProcessInstancePO.setOrganizationName(extProcessInstancePO.getStartUserInfo().getOrganizationName());
            }
            extProcessInstanceMapper.insert(extProcessInstancePO);
        }

    }

    public boolean setStartUserName(ExtProcessInstancePO processInstancePO) {
        StartUserInfo userInfo = processInstancePO.getStartUserInfo();
        if (ObjectUtils.isNotEmpty(userInfo) && ObjectUtils.isNotEmpty(userInfo.getStartUserId())
            && ObjectUtils.isEmpty(userInfo.getStartUserName())) {
            UserDTO user = userClient.getUser(userInfo.getStartUserId());
            if (ObjectUtils.isNotEmpty(user)) {
                userInfo.setStartUserName(ObjectUtils.isEmpty(user) ? "" : user.getName());
                if (ObjectUtils.isNotEmpty(user.getOrganization())) {
                    userInfo.setOrganizationId(user.getOrganization().getId());
                    userInfo.setOrganizationName(user.getOrganization().getName());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Map<String, ExtProcessInstance> getBatch(Collection<String> processInstanceIds) {
        if (CollectionUtils.isEmpty(processInstanceIds)) {
            return new HashMap<>();
        }
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
                .processInstanceIds(new HashSet<>(processInstanceIds)).includeProcessVariables().list();
        if (CollectionUtils.isEmpty(processInstanceList)) {
            return new HashMap<>();
        }
        List<ExtProcessInstancePO> extProcessInstancePOS = extProcessInstanceMapper.selectList(Wrappers.<ExtProcessInstancePO>lambdaQuery()
                                                                                                       .in(ExtProcessInstancePO::getId, processInstanceIds));
        if (CollectionUtils.isEmpty(extProcessInstancePOS)) {
            return new HashMap<>();
        }
        Map<String, ExtProcessInstancePO> processInstancePOMap = extProcessInstancePOS.stream()
                .collect(Collectors.toMap(ExtProcessInstancePO::getId, Function.identity()));
        return processInstanceList.stream().filter(t -> processInstancePOMap.containsKey(t.getProcessInstanceId())).map(t -> {
            ExtProcessInstancePO extProcessInstancePO = processInstancePOMap.get(t.getProcessInstanceId());
            Object result = t.getProcessVariables().get(VariableConstant.RESULT);
            Object data = t.getProcessVariables().get(VariableConstant.DATA);
            return ExtProcessInstance.builder()
                    .id(t.getProcessInstanceId())
                    .processDefinitionKey(extProcessInstancePO.getProcessDefinitionKey())
                    .processInstance(t)
                    .businessId(extProcessInstancePO.getBusinessId())
                    .instanceName(extProcessInstancePO.getInstanceName())
                    .applicationId(extProcessInstancePO.getApplicationId())
                    .startUserInfo(extProcessInstancePO.getStartUserInfo())
                    .data(data)
                    .result(Objects.isNull(result) ? null : result.toString())
                    .dataMap(JSON.parseObject(extProcessInstancePO.getData(), new TypeReference<Map<String, Object>>() {}))
                    .extProcessInstancePO(extProcessInstancePO)
                    .build();
        }).collect(Collectors.toMap(ExtProcessInstance::getId, Function.identity()));
    }

    @Override
    public Optional<ExtHistoricProcessInstance> getHistoric(String id) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().includeProcessVariables()
                .processInstanceId(id).singleResult();
        if (Objects.isNull(historicProcessInstance)) {
            return Optional.empty();
        }
        ExtProcessInstancePO extProcessInstancePO = extProcessInstanceMapper.selectById(id);
        Object result = historicProcessInstance.getProcessVariables().get(VariableConstant.RESULT);
        Object data = historicProcessInstance.getProcessVariables().get(VariableConstant.DATA);
        Object sourceProcessId = historicProcessInstance.getProcessVariables().get(VariableConstant.SOURCE_PROCESS_ID);
        return Optional.of(
                ExtHistoricProcessInstance.builder()
                        .processDefinitionKey(extProcessInstancePO.getProcessDefinitionKey())
                        .processInstance(historicProcessInstance)
                        .businessId(extProcessInstancePO.getBusinessId())
                        .applicationId(extProcessInstancePO.getApplicationId())
                        .startUserInfo(extProcessInstancePO.getStartUserInfo())
                        .processInstanceName(extProcessInstancePO.getInstanceName())
                        .data(data)
                        .result(Objects.isNull(result) ? null : result.toString())
                        .sourceProcessId(Objects.isNull(sourceProcessId) ? null : sourceProcessId.toString())
                        .build());
    }

    @Override
    public Optional<ExtProcessInstance> get(String id) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(id).includeProcessVariables().singleResult();
        if (Objects.isNull(processInstance)) {
            return Optional.empty();
        }
        ExtProcessInstancePO extProcessInstancePO = extProcessInstanceMapper.selectById(id);
        if (ObjectUtils.isEmpty(extProcessInstancePO)) {
            return Optional.empty();
        }
        Object result = processInstance.getProcessVariables().get(VariableConstant.RESULT);
        Object data = processInstance.getProcessVariables().get(VariableConstant.DATA);
        return Optional.of(ExtProcessInstance.builder()
                                   .processDefinitionKey(extProcessInstancePO.getProcessDefinitionKey())
                                   .processInstance(processInstance)
                                   .instanceName(extProcessInstancePO.getInstanceName())
                                   .businessId(extProcessInstancePO.getBusinessId())
                                   .applicationId(extProcessInstancePO.getApplicationId())
                                   .startUserInfo(extProcessInstancePO.getStartUserInfo())
                                   .data(data)
                                   .result(Objects.isNull(result) ? null : result.toString())
                                   .dataMap(JSON.parseObject(extProcessInstancePO.getData(), new TypeReference<Map<String, Object>>() {}))
                                   .extProcessInstancePO(extProcessInstancePO)
                                   .build());
    }

    @Override
    public Optional<ExtProcessInstance> getByBusinessKey(String businessKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey)
                .includeProcessVariables().singleResult();
        if (Objects.isNull(processInstance)) {
            return Optional.empty();
        }
        ExtProcessInstancePO extProcessInstancePO = extProcessInstanceMapper.selectById(processInstance.getId());
        Object result = processInstance.getProcessVariables().get(VariableConstant.RESULT);
        return Optional.of(ExtProcessInstance.builder()
                                   .processDefinitionKey(extProcessInstancePO.getProcessDefinitionKey())
                                   .processInstance(processInstance)
                                   .businessId(extProcessInstancePO.getBusinessId())
                                   .applicationId(extProcessInstancePO.getApplicationId())
                                   .startUserInfo(extProcessInstancePO.getStartUserInfo())
                                   .data(extProcessInstancePO.getData())
                                   .result(Objects.isNull(result) ? null : result.toString())
                                   .build());
    }
}
