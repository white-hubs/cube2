package com.guhecloud.cube.base.flow.flowable.definition.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.activiti.bpmn.model.Process;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessDefinitionDTO {

    private String id;
    private String key;
    private String name;
    private Integer version;
    private String tenantId;
    private String deploymentId;
    private Process process;
    private String processFormJson;
}
