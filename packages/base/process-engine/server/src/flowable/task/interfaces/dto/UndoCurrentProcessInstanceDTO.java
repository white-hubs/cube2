package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import lombok.Data;

/**
 * @author qiu wei jun
 * @date 2022/9/30 14:06:51
 */
@Data
public class UndoCurrentProcessInstanceDTO {
    private String processInstanceId;
    private String comment;
}
