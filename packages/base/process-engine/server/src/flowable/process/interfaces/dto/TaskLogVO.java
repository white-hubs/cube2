package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/5/27 13:36
 * @Description:
 */
@Data
public class TaskLogVO {
    private String processInstanceId;
    /**
     * 节点唯一标识
     */
    private String nodeKey;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作时间
     */
    private LocalDateTime operationTime;
}
