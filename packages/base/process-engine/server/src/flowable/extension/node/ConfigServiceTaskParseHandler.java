package com.guhecloud.cube.base.flow.flowable.extension.node;

import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.mapper.TaskNodeMapper;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po.TaskNodePO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.Data;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author pengjian
 * @date 2022/8/22 11:45
 * @description
 */
@Component
public class ConfigServiceTaskParseHandler extends ServiceTaskParseHandler implements ConfigTaskParseHandler {

    public static final String CONFIG = "activiti:config";
    public static boolean flag = true;

    @Data
    public static class Stu {
        private String age;
        private String name;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, ServiceTask serviceTask) {
        super.executeParse(bpmnParse, serviceTask);
        if (!flag) {
            return;
        }
        TaskNodeMapper taskNodeMapper = ApplicationContextHolder.getBean(TaskNodeMapper.class);
        TaskNodePO nodePO = getCurrentTaskNode(serviceTask.getId(), bpmnParse.getCurrentProcessDefinition().getKey(), serviceTask);

        Iterator<Map.Entry<String, List<ExtensionElement>>> entryIterator = serviceTask.getExtensionElements().entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, List<ExtensionElement>> next = entryIterator.next();

            String key = next.getKey();

            if (key.equals("config")) {
                //  保存节点的配置
                taskNodeMapper.updateById(nodePO);
            }
        }
    }

    public ExtensionElement parseUserTaskOperations(BpmnParse bpmnParse, UserTask userTask) {
        //获取扩展属性标签元素
        List<ExtensionElement> tasks = userTask.getExtensionElements()
                .get("serviceTask");
        if (CollectionUtils.isEmpty(tasks)) {
            return null;
        } else {
            return tasks.get(0);
        }
    }
}
