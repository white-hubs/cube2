package com.guhecloud.cube.base.flow.flowable.definition.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.definition.application.commandgateway.command.DeployProcessDefinitionCommand;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;
import com.guhecloud.cube.base.flow.flowable.definition.domain.repository.ProcessDefinitionRepository;
import com.guhecloud.cube.base.flow.flowable.definition.interfaces.dto.FlowEngineDTO;
import com.guhecloud.cube.base.flow.flowable.resolver.WorkFlowConstants;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.base.flow.flowable.resolver.BpmnResolver.connect;
import static com.guhecloud.cube.base.flow.flowable.resolver.BpmnResolver.create;
import static com.guhecloud.cube.base.flow.flowable.resolver.BpmnResolver.createEndEvent;
import static com.guhecloud.cube.base.flow.flowable.resolver.BpmnResolver.createStartEvent;

/**
 * @author pengjian
 * @date 2022/8/30 15:49
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessDefinitionCommandService {

    private final RepositoryService repositoryService;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public ProcessDefinitionDTO deploy(DeployProcessDefinitionCommand deployProcessDefinitionCommand) throws IOException {
        Deployment deployment = repositoryService.createDeployment()
                .tenantId(String.valueOf(SessionContextHolder.getRequester().getTenant()))
                .addBytes(deployProcessDefinitionCommand.getBpmnXml().getName(), deployProcessDefinitionCommand.getBpmnXml().getBytes())
                .deploy();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        // todo 解决多次部署问题
        processDefinitionRepository.save(
                new ExtProcessDefinition(
                        pd.getKey(), deployProcessDefinitionCommand.getFormKey()
                )
                                        );

        return ProcessDefinitionDTO.builder()
                .id(pd.getId())
                .key(pd.getKey())
                .name(pd.getName())
                .version(pd.getVersion())
                .tenantId(pd.getTenantId())
                .deploymentId(pd.getDeploymentId())
                .build();
    }

    public ProcessDefinitionDTO deployByJSON(FlowEngineDTO flowEngineDTO) {
        String processJson = flowEngineDTO.getProcess();
        ChildNode childNode = JsonUtils.deserialize(processJson, new TypeReference<ChildNode>() {});
        String remark = flowEngineDTO.getRemark();
        String formName = flowEngineDTO.getFormName();
        Integer groupId = flowEngineDTO.getGroupId();
        String templateId = IdWorker.getIdStr();
        BpmnModel bpmnModel = assemBpmnModel(processJson, childNode, remark, formName, groupId,
                                             templateId);
        Deployment pd = repositoryService.createDeployment()
                .addBpmnModel(formName + ".bpmn", bpmnModel)
                .name(formName)
                .key(bpmnModel.getMainProcess().getId())
                .tenantId(SessionContextHolder.getRequester().getTenant().toString())
                .category(groupId + "")
                .deploy();

        return ProcessDefinitionDTO.builder()
                .id(pd.getId())
                .key(pd.getKey())
                .name(pd.getName())
                .version(pd.getVersion())
                .tenantId(pd.getTenantId())
                .deploymentId(templateId)
                .build();
    }

    private BpmnModel assemBpmnModel(String processJson, ChildNode childNode, String remark,
                                     String formName, Integer groupId, String templateId
                                    ) {
        BpmnModel bpmnModel = new BpmnModel();
        List<SequenceFlow> sequenceFlows = new ArrayList<>();
        Map<String, ChildNode> childNodeMap = new HashMap<>();
        bpmnModel.setTargetNamespace(groupId + "");
        ExtensionAttribute extensionAttribute = new ExtensionAttribute();
        extensionAttribute.setName(ProcessConstant.PROCESS_FORM_JSON);
        extensionAttribute.setNamespace(ProcessConstant.NAMESPACE);
        extensionAttribute.setValue(processJson);
        Process process = new Process();
        process.setId(WorkFlowConstants.PROCESS_PREFIX + templateId);
        process.setName(formName);
        process.setDocumentation(remark);
        process.addAttribute(extensionAttribute);
        bpmnModel.addProcess(process);

        StartEvent startEvent = createStartEvent();
        process.addFlowElement(startEvent);
        String lastNode = null;
        try {
            lastNode = create(startEvent.getId(), childNode, process, bpmnModel, sequenceFlows, childNodeMap);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ServiceException("操作失败");
        }
        EndEvent endEvent = createEndEvent();
        process.addFlowElement(endEvent);
        process.addFlowElement(connect(lastNode, endEvent.getId(), sequenceFlows, childNodeMap, process));
        List<ActivitiListener> executionListeners = new ArrayList<>();
        ActivitiListener flowableListener = new ActivitiListener();
        flowableListener.setEvent(ExecutionListener.EVENTNAME_END);
        flowableListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        flowableListener.setImplementation("${processListener}");
        executionListeners.add(flowableListener);
        new BpmnAutoLayout(bpmnModel).execute();
        return bpmnModel;
    }

}
