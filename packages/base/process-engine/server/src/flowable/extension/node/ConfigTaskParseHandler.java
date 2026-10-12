package com.guhecloud.cube.base.flow.flowable.extension.node;

import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.mapper.TaskNodeMapper;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po.TaskNodePO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.Task;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/8/26 9:45
 * @description
 */
@Component
@DependsOn({ "applicationContextHolder" })
public interface ConfigTaskParseHandler {
    default TaskNodePO getCurrentTaskNode(String nodeId, String processDefinitionKey, Task task) {
        TaskNodeMapper taskNodeMapper = ApplicationContextHolder.getBean(TaskNodeMapper.class);
        TaskNodePO nodePO = taskNodeMapper.selectById(nodeId);

        if (Objects.isNull(nodePO)) {
            TaskNodePO taskNodePO = new TaskNodePO();
            taskNodePO.setNodeType(NodeType.APPROVAL);
            taskNodePO.setTaskKey(nodeId);
            taskNodePO.setProcessDefinitionKey(processDefinitionKey);
            taskNodeMapper.insert(taskNodePO);
            nodePO = taskNodePO;
        }

        Iterator<Map.Entry<String, List<ExtensionElement>>> entryIterator = task.getExtensionElements().entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, List<ExtensionElement>> next = entryIterator.next();

            String key = next.getKey();
            List<ExtensionElement> value = next.getValue();

            if (key.equals("config")) {
                // todo 保存节点的配置
                taskNodeMapper.updateById(nodePO);
            }
        }

        return nodePO;
    }
}
