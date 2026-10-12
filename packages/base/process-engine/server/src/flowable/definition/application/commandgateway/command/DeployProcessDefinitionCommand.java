package com.guhecloud.cube.base.flow.flowable.definition.application.commandgateway.command;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author pengjian
 * @date 2022/8/30 13:46
 * @description
 */
@Data
public class DeployProcessDefinitionCommand {

    private MultipartFile bpmnXml;

    private String formKey;

    /**
     * 流程全局配置
     */

}
