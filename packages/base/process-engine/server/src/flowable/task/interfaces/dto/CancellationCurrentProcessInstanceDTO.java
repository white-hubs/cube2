package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import lombok.Data;

/**
 * @author qiu wei jun
 * @date 2022/9/30 13:38:46
 */
@Data
public class CancellationCurrentProcessInstanceDTO {
    private String taskId;
    private String comment;
}
