package com.guhecloud.cube.base.flow.flowable.scene.approval.application.commandgateway.command;

import com.guhecloud.cube.base.flow.flowable.definition.application.commandgateway.command.DeployProcessDefinitionCommand;
import com.guhecloud.cube.base.flow.flowable.definition.domain.UserTaskNode;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/30 16:00
 * @description 审批场景流程部署
 */
@Data
public class DeployApprovalSceneDefinitionCommand extends DeployProcessDefinitionCommand {
    /**
     * 流程的任务节点
     */
    private List<UserTaskNode> taskNodes;
}
