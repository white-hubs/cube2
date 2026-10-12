package com.guhecloud.cube.base.flow.flowable.definition.application.querygateway;

import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author pengjian
 * @date 2022/8/31 11:50
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProcessDefinitionQueryService {
    private final RepositoryService repositoryService;

    /**
     * 获取流程定义资源。
     *
     * @param processDefineId 流程定义ID
     * @param type            1-获取xml, 2-获取图片
     *
     * @return 文件流
     */
    public InputStream getProcessDefineResource(String processDefineId, int type) {
        //获取BpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefineId);
        //创建转换对象
        BpmnXMLConverter converter = new BpmnXMLConverter();
        //把bpmnModel对象转换成字符
        byte[] bytes = converter.convertToXML(bpmnModel);
        return new ByteArrayInputStream(bytes);
    }

    public ProcessDefinitionDTO get(@NotBlank String processDefinitionKey) {
        long startTime = System.currentTimeMillis();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        Process process = bpmnModel.getProcessById(processDefinition.getKey());
        String dingDing = process.getAttributeValue(ProcessConstant.NAMESPACE, ProcessConstant.PROCESS_FORM_JSON);
        log.info("查询流程定义耗时：{}ms", System.currentTimeMillis() - startTime);
        return new ProcessDefinitionDTO(
                processDefinition.getId(),
                processDefinition.getKey(),
                processDefinition.getName(),
                processDefinition.getVersion(),
                processDefinition.getTenantId(),
                processDefinition.getDeploymentId(), process, dingDing);

    }
}
