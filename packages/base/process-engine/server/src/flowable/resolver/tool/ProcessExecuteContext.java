package com.guhecloud.cube.base.flow.flowable.resolver.tool;

import base.constant.VariableConstant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.context.config.ActivityDeploymentCache;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.po.FormFlowBusinessPO;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowQueryService;
import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;
import com.guhecloud.cube.base.flow.flowable.definition.domain.repository.ProcessDefinitionRepository;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.Properties;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/3/2 13:36
 * @Description: 流程执行上下文
 */
public class ProcessExecuteContext {
    private static final ThreadLocal<ProcessInfo> PROCESS_INFO_THREAD_LOCAL = new ThreadLocal<>();

    public static Properties getProcessNodeProperties(String processInsId, String activityId) {
        ProcessInfo processInfo = PROCESS_INFO_THREAD_LOCAL.get();
        if (processInfo != null) {
            ChildNode childNode = processInfo.getChildNode();
            if (childNode != null) {
                Properties props = ExtProcessDefinition.getChildNode(childNode, activityId).getProps();
                return props;
            }
        }
        Optional<ExtProcessInstance> extProcessInstance = ApplicationContextHolder.getBean(ExtProcessInstanceRepository.class)
                .get(processInsId);
        if (extProcessInstance.isEmpty()) {
            throw new RuntimeException("流程实例不存在");
        }
        initInfo(extProcessInstance.get());
        return getProcessNodeProperties(processInsId, activityId);
    }

    public static void initInfo(ExtProcessInstance extProcessInstance) {
        String processDefinitionId = extProcessInstance.getProcessInstance().getProcessDefinitionId();
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setId(extProcessInstance.getProcessInstance().getProcessInstanceId());
        processInfo.setProcessDefinitionId(processDefinitionId);
        processInfo.setInstanceName(extProcessInstance.getInstanceName());
        processInfo.setBusinessId(extProcessInstance.getBusinessId());
        processInfo.setApplicationId(extProcessInstance.getApplicationId());
        processInfo.setStartUserInfo(extProcessInstance.getStartUserInfo());
        processInfo.setStartTime(DateUtils.convert(extProcessInstance.getProcessInstance().getStartTime()));
        Process mainProcess = ApplicationContextHolder.getBean(RepositoryService.class).getBpmnModel(processDefinitionId).getMainProcess();
        String processJson = mainProcess.getAttributeValue(ProcessConstant.NAMESPACE, ProcessConstant.PROCESS_FORM_JSON);
        processInfo.setChildNode(JsonUtils.deserialize(processJson, new TypeReference<ChildNode>() {}));
        //获取是否去重配置
        FormFlowDetailVO formFlowDetailVO = ApplicationContextHolder.getBean(FormFlowQueryService.class)
                .getFromInfo(extProcessInstance.getBusinessId().getBusinessType());
        processInfo.setDeDuplication(formFlowDetailVO.getAutoRemoveDuplication());
        processInfo.setFormJson(formFlowDetailVO.getFormJson());
        processInfo.setBacklogTitle(extProcessInstance.getInstanceName());
        processInfo.setExtProcessInstancePO(extProcessInstance.getExtProcessInstancePO());
        PROCESS_INFO_THREAD_LOCAL.set(processInfo);
    }

    public static ProcessInfo getProcessInfo(String processInstanceId) {
        ProcessInfo processInfo = PROCESS_INFO_THREAD_LOCAL.get();
        if (processInfo == null) {
            Optional<ExtProcessInstance> extProcessInstance = ApplicationContextHolder.getBean(ExtProcessInstanceRepository.class)
                    .get(processInstanceId);
            extProcessInstance.ifPresent(ProcessExecuteContext::initInfo);
        }
        return PROCESS_INFO_THREAD_LOCAL.get();
    }
    public static void remove() {
        PROCESS_INFO_THREAD_LOCAL.remove();
        ActivityDeploymentCache.clearLocalCache();
    }

    public static void initLocalCache() {
        ActivityDeploymentCache.initLocalCache();
    }
}
