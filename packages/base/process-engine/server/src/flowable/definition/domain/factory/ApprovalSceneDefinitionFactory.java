package com.guhecloud.cube.base.flow.flowable.definition.domain.factory;

import com.guhecloud.cube.base.flow.flowable.definition.domain.ApprovalSceneDefinition;
import com.guhecloud.cube.base.flow.flowable.scene.approval.application.commandgateway.command.DeployApprovalSceneDefinitionCommand;

/**
 * @author pengjian
 * @date 2022/8/30 17:07
 * @description
 */
public class ApprovalSceneDefinitionFactory {
    public static ApprovalSceneDefinition create(DeployApprovalSceneDefinitionCommand deployApprovalSceneDefinitionCommand, String key) {
        ApprovalSceneDefinition approvalSceneDefinition = new ApprovalSceneDefinition(key
                , deployApprovalSceneDefinitionCommand.getFormKey());
        approvalSceneDefinition.changeTaskNodes(deployApprovalSceneDefinitionCommand.getTaskNodes());
        return approvalSceneDefinition;
    }
}
