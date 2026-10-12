package com.guhecloud.cube.base.flow.flowable.definition.domain;

import com.guhecloud.cube.base.flow.flowable.definition.domain.entity.UserTaskNodeConfig;
import com.guhecloud.cube.base.flow.flowable.extension.node.NodeType;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/30 9:29
 * @description
 */
@Data
public class TaskNode {

    private String taskKey;

    private String taskName;

    private NodeType nodeType;

    private String processDefinitionKey;

    private UserTaskNodeConfig userTaskNodeConfig;

}
