package com.guhecloud.cube.base.flow.form.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author bq
 * @date 2022/10/25
 * @description 表单控件与流程节点配置操作权限
 */
@Data
public class FormControlFlowCommand {

    private Long id;

    /**
     * 流程定义key
     */
    @NotEmpty(message = "必须指定流程定义key")
    private String flowKey;

    /**
     * 任务节点信息
     */
    private String taskKey;

    /**
     * json配置
     */
    private String config;

}