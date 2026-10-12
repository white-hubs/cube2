package com.guhecloud.cube.base.flow.flowable.process.domain;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.mapper.TaskNodeMapper;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po.TaskNodePO;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.task.application.TaskRuNodeService;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskRuNodePO;
import jakarta.annotation.Resource;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/9/5 20:20
 * @description
 */
@Named
public class ExtProcessInstanceDomainService {

    @Resource
    private ExtProcessInstanceRepository extProcessInstanceRepository;
    @Resource
    private TaskNodeMapper taskNodeMapper;
    @Resource
    private TaskRuNodeService taskRuNodeService;

    public void save(ExtProcessInstance extProcessInstance) {

        extProcessInstanceRepository.save(extProcessInstance);

        // 调用任务聚合加载配置

        // 调用实例聚合加载配置
        loadConfig(extProcessInstance.getProcessDefinitionKey(), extProcessInstance.getProcessInstance().getProcessInstanceId());

    }

    /**
     * @param processDefinitionKey
     */
    private void loadConfig(String processDefinitionKey, String processInstanceId) {
        /**
         * 1. 读取流程全局配置
         * 2. 查询流程下相关任务节点的配置
         */
        List<TaskNodePO> taskNodes = taskNodeMapper.selectList(
                Wrappers.<TaskNodePO>lambdaQuery()
                        .eq(TaskNodePO::getProcessDefinitionKey, processDefinitionKey)
                                                              );

        List<TaskRuNodePO> taskInstances = new ArrayList<>(taskNodes.size());
        taskNodes.forEach(taskNode -> {
            TaskRuNodePO taskRuNodePO = new TaskRuNodePO();
            taskRuNodePO.setProcessInstanceId(processInstanceId);
            taskRuNodePO.setTaskKey(taskNode.getTaskKey());
            taskRuNodePO.setTaskConfig(taskNode.getConfig());
            taskRuNodePO.setProcessDefinitionKey(taskNode.getProcessDefinitionKey());
            taskInstances.add(taskRuNodePO);
        });

        taskRuNodeService.saveBatch(taskInstances);
    }

}
