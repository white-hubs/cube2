package com.guhecloud.cube.base.flow.client.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-05-17  11:46
 * @Description:
 * @Version: 1.0
 */
@Data
public class TaskVariableDTO {
    /**
     * 任务id
     */
    private String id;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 流程实例名称
     */
    private String procInstName;
    /**
     * 任务创建时间
     */
    private LocalDateTime createTime;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 变量值
     */
    private String test;
    private Long assignee;
    private Integer tenantId;
    /**
     * 节点id
     */
    private String nodeId;
}
