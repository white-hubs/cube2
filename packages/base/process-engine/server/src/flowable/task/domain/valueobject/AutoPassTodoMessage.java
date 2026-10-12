package com.guhecloud.cube.base.flow.flowable.task.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 删除自动通过待办消息
 * @date 2023/7/2 10:52
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class AutoPassTodoMessage {
    /**
     * 节点id
     */
    private String id;
    private String taskDefinitionKey;
    private String processInstanceId;

}
