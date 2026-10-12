package com.guhecloud.cube.base.flow.flowable.definition.interfaces;

import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.definition.application.commandgateway.ProcessDefinitionCommandService;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.application.querygateway.ProcessDefinitionQueryService;
import com.guhecloud.cube.base.flow.flowable.definition.domain.NewApprovalNode;
import com.guhecloud.cube.base.flow.flowable.definition.domain.UserTaskNode;
import com.guhecloud.cube.base.flow.flowable.definition.domain.entity.UserTaskNodeConfig;
import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.ApprovalCommentConfig;
import com.guhecloud.cube.base.flow.flowable.definition.interfaces.dto.FlowEngineDTO;
import com.guhecloud.cube.base.flow.flowable.definition.interfaces.param.DeployProcessDefinitionParam;
import com.guhecloud.cube.base.flow.flowable.extension.node.NodeType;
import com.guhecloud.cube.base.flow.flowable.scene.approval.application.commandgateway.ApprovalSceneDefinitionCommandService;
import com.guhecloud.cube.base.flow.flowable.scene.approval.application.commandgateway.command.DeployApprovalSceneDefinitionCommand;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/22 13:02
 * @description
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessDefinitionController {

    private final ApprovalSceneDefinitionCommandService approvalSceneDefinitionCommandService;
    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;

    private final ProcessDefinitionCommandService processDefinitionCommandService;

    private final ProcessDefinitionQueryService processDefinitionQueryService;

    @GetMapping("/process/definition/xml")
    public void getProcessDefineXML(HttpServletResponse response,
                                    @RequestParam("processDefineId") String processDefineId
                                   ) {
        InputStream inputStream = processDefinitionQueryService.getProcessDefineResource(processDefineId, 1);

        response.setContentType("text/xml");
        try {
            OutputStream outputStream = response.getOutputStream();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/process/definition/key")
    public ProcessDefinitionDTO getProcessDefinition(@RequestParam("key") @NotBlank String key) {
        return processDefinitionQueryService.get(key);
    }

    @GetMapping("/process/definition/business-key")
    public ProcessDefinitionDTO getProcessDefinitionByBusinessKey(@RequestParam("businessKey") @NotBlank String businessKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey)
                .singleResult();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(processInstance.getProcessDefinitionId()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        Process process = bpmnModel.getProcessById(processInstance.getProcessDefinitionKey());
        String dingDing = process.getAttributeValue(ProcessConstant.NAMESPACE, ProcessConstant.PROCESS_FORM_JSON);
        return ProcessDefinitionDTO.builder()
                .id(processDefinition.getId())
                .key(processDefinition.getKey())
                .name(processDefinition.getName())
                .version(processDefinition.getVersion())
                .tenantId(processDefinition.getTenantId())
                .deploymentId(processDefinition.getDeploymentId())
                .processFormJson(dingDing)
                .build();
    }

    @PostMapping("/process/definition/deploy/json")
    public ProcessDefinitionDTO deployProcessDefinitionByJson(@RequestBody FlowEngineDTO flowEngineDTO) {
        return processDefinitionCommandService.deployByJSON(flowEngineDTO);
    }

    /**
     * 仅流程
     */
    @PostMapping("/process/definition/deploy")
    public ProcessDefinitionDTO deployProcessDefinition(@RequestParam("bpmn") MultipartFile multipartFile, String data) {

        DeployProcessDefinitionParam deployProcessDefinitionParam = JsonUtils.deserialize(data, DeployProcessDefinitionParam.class);
        DeployApprovalSceneDefinitionCommand deployApprovalSceneDefinitionCommand = new DeployApprovalSceneDefinitionCommand();

        deployApprovalSceneDefinitionCommand.setBpmnXml(multipartFile);
        deployApprovalSceneDefinitionCommand.setFormKey(deployProcessDefinitionParam.getFormKey());

        // 解析
        List<UserTaskNode> taskNodes = new ArrayList<>();
        List<DeployProcessDefinitionParam.ApprovalTaskNodeParam> taskNodeParams = deployProcessDefinitionParam.getTaskNodes();
        taskNodeParams.forEach(taskNodeParam -> {

            if (taskNodeParam.getNodeType() == NodeType.APPROVAL) {
                UserTaskNode userTaskNode = ObjectUtils.copying(taskNodeParam, UserTaskNode.class);
                NewApprovalNode newApprovalNode = new NewApprovalNode();
                newApprovalNode.setApprovalWay(taskNodeParam.getApprovalWay());
                newApprovalNode.setData(taskNodeParam.getData());
                newApprovalNode.setApprovalCommentConfig(new ApprovalCommentConfig(taskNodeParam.isSupportText(), taskNodeParam.isSupportHandwrittenSignature()));
                UserTaskNodeConfig userTaskNodeConfig = UserTaskNodeConfig.builder()
                        .copy(taskNodeParam.getCopy())
                        .newApprovalNode(newApprovalNode)
                        .build();

                userTaskNode.setUserTaskNodeConfig(userTaskNodeConfig);
                // 解析抄送用户
                taskNodes.add(
                        userTaskNode
                             );
            }
        });

        deployApprovalSceneDefinitionCommand.setTaskNodes(taskNodes);
        ProcessDefinitionDTO processDefinitionDTO = approvalSceneDefinitionCommandService.deploy(
                deployApprovalSceneDefinitionCommand
                                                                                                );
        return processDefinitionDTO;
    }

    /**
     * 发布流程定义 流程+表单
     *
     * @param multipartFile xml文件
     *
     * @return
     *
     * @throws IOException
     */
    @PostMapping("/process/definition-with-form/deploy")
    public ProcessDefinitionDTO deployProcessDefinitionWithForm(@RequestParam("bpmn") MultipartFile multipartFile, String data) {
        //todo 保存表单
        // 保存流程
        DeployApprovalSceneDefinitionCommand deployApprovalSceneDefinitionCommand = new DeployApprovalSceneDefinitionCommand();
        deployApprovalSceneDefinitionCommand.setBpmnXml(multipartFile);
        // todo 保存表单主键
        deployApprovalSceneDefinitionCommand.setFormKey(null);
        ProcessDefinitionDTO processDefinitionDTO = approvalSceneDefinitionCommandService.deploy(
                deployApprovalSceneDefinitionCommand
                                                                                                );
        return processDefinitionDTO;
    }

}
