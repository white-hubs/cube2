package com.guhecloud.cube.base.flow.flowable.task.application.dto;

import lombok.Data;

/**
 * @author pengjian
 * @date 2022/10/25 19:21
 * @description
 */
@Data
public class CopyLogDTO {
    private String taskId;
    private String processInstanceId;
    private String userIds;
}
