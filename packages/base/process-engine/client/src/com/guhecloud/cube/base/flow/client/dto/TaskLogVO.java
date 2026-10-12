package com.guhecloud.cube.base.flow.client.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 历史节点日志
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
