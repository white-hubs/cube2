package com.guhecloud.cube.base.flow.flowable.task.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengjian
 * @date 2022/9/2 16:41
 * @description
 */
@Getter
@AllArgsConstructor
public class TaskInstanceKey {
    private String taskKey;
    private String processInstanceId;
}
