package com.guhecloud.cube.base.flow.flowable.process.application.service;

import com.guhecloud.cube.base.flow.flowable.process.application.dto.ProcessInstanceDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author jianqiu
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessInstanceService {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;

    public ProcessInstanceDTO startProcessInstance(String userId, String processDefinitionKey, String businessType, String businessKey,
                                                   Map<String, Object> variables
                                                  ) {
        String tenantId = String.valueOf(SessionContextHolder.getRequester().getTenant());
        // 如果businessKey不为空，则校验是否已存在对应的流程实例（运行时和历史），存在则不能启动流程
        if (StringUtils.isNotEmpty(businessKey)) {
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey)
                    .list();
            if (CollectionUtils.isNotEmpty(processInstances)) {
                throw new ServiceException("已存在businessKey对应的流程实例，无法再次启动");
            }
            List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceBusinessKey(businessKey).list();
            if (CollectionUtils.isNotEmpty(historicProcessInstances)) {
                throw new ServiceException("已存在businessKey对应的流程实例，无法再次启动");
            }
        }
        // 设置发起人（START_USER_ID）
        Authentication.setAuthenticatedUserId(userId);
        // 启动流程
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .processDefinitionTenantId(tenantId)
                .latestVersion()
                .singleResult();
        if (Objects.isNull(processDefinition)) {
            throw new ServiceException("流程定义不存在");
        }
        long currentTimeMillis = System.currentTimeMillis();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), businessKey, variables);
        log.info("启动流程实例耗时：{}ms", System.currentTimeMillis() - currentTimeMillis);

        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setId(processInstance.getId());
        dto.setKey(processDefinition.getKey());
        dto.setName(processDefinition.getName());
        dto.setVersion(processDefinition.getVersion());
        dto.setTenantId(processDefinition.getTenantId());
        dto.setBusinessKey(processInstance.getBusinessKey());
        dto.setStartTime(DateUtils.parse(processInstance.getStartTime()));
        return dto;
    }

    /**
     * 获取流程实例完整信息（注意，扩展的信息较多，不要在循环中调用，否则性能下降明显）
     */

    public ProcessInstanceDTO getProcessInstance(String processInstanceId, String businessKey) {
        if (StringUtils.isEmpty(processInstanceId) && StringUtils.isEmpty(businessKey)) {
            throw new ServiceException("processInstanceId与businessKey不能同时为空");
        }
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        if (StringUtils.isNotEmpty(processInstanceId)) {
            query.processInstanceId(processInstanceId);
        }
        if (StringUtils.isNotEmpty(businessKey)) {
            query.processInstanceBusinessKey(businessKey);
        }
        HistoricProcessInstance hpi = query.singleResult();
        if (hpi == null) {
            return null;
        }
        // ProcessInstanceDTO
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setId(hpi.getId());
        dto.setKey(hpi.getProcessDefinitionKey());
        dto.setName(hpi.getProcessDefinitionName());
        dto.setVersion(hpi.getProcessDefinitionVersion());
        dto.setTenantId(hpi.getTenantId());
        dto.setBusinessKey(hpi.getBusinessKey());
        dto.setStartTime(DateUtils.parse(hpi.getStartTime()));
        dto.setEndTime(Objects.nonNull(hpi.getEndTime()) ? DateUtils.parse(hpi.getEndTime()) : null);
        dto.setDeleteReason(hpi.getDeleteReason());
        return dto;
    }
}
