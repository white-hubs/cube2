package com.guhecloud.cube.base.flow.flowable.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengjian
 * @date 2022/10/24 14:15
 * @description
 */
@Getter
@AllArgsConstructor
public class DataObject {
    private String taskId;
    private String processInstanceId;
    private Object data;
}
