package com.guhecloud.cube.base.flow.flowable.scene.approval.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.flowable.definition.domain.ApprovalSceneDefinition;
import com.guhecloud.cube.base.flow.flowable.definition.domain.UserTaskNode;
import com.guhecloud.cube.base.flow.flowable.definition.domain.repository.ApprovalSceneDefinitionRepository;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.mapper.TaskNodeMapper;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po.TaskNodePO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/30 17:05
 * @description
 */
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultApprovalSceneDefinitionRepository implements ApprovalSceneDefinitionRepository {
    private final TaskNodeMapper taskNodeMapper;

    @Override
    public void save(ApprovalSceneDefinition approvalSceneDefinition) {

        taskNodeMapper.delete(
                Wrappers.<TaskNodePO>lambdaQuery()
                        .eq(TaskNodePO::getProcessDefinitionKey, approvalSceneDefinition.getKey())
                             );

        // 持久化数据
        List<UserTaskNode> taskNodes = approvalSceneDefinition.getTaskNodes();
        if (CollectionUtils.isNotEmpty(taskNodes)) {
            taskNodes.forEach(taskNode -> {
                TaskNodePO taskNodePO = ObjectUtils.copying(taskNode, TaskNodePO.class);
                taskNodePO.setConfig(taskNode.getUserTaskNodeConfig());
                taskNodePO.setTaskKey(taskNode.getTaskKey());
                taskNodePO.setNodeType(taskNode.getNodeType());
                taskNodePO.setProcessDefinitionKey(taskNode.getProcessDefinitionKey());
                taskNodeMapper.insert(taskNodePO);
            });
        }
    }
}
