package com.guhecloud.cube.base.flow.flowable.definition.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/30 17:02
 * @description
 */
@Getter
@Setter
public class ApprovalSceneDefinition extends ExtProcessDefinition {

    private List<UserTaskNode> taskNodes;

    public ApprovalSceneDefinition(String key) {
        super(key);
    }

    public ApprovalSceneDefinition(String key, String formKey) {
        super(key);
        super.setFormKey(formKey);
    }

    public void changeTaskNodes(List<UserTaskNode> taskNodes) {
        this.taskNodes = taskNodes;
        this.taskNodes.forEach(taskNode -> taskNode.setProcessDefinitionKey(super.getKey()));
    }
}
