package com.guhecloud.cube.base.flow.flowable.extension.node;

import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.mapper.TaskNodeMapper;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po.TaskNodePO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author pengjian
 * @date 2022/8/22 11:45
 * @description
 */
@Slf4j
@DependsOn({ "applicationContextHolder" })
@Component
public class ConfigUserTaskParseHandler extends UserTaskParseHandler implements ConfigTaskParseHandler {

    public static boolean flag = true;

    public static final String CONFIG = "activiti:config";

    @Data
    public static class Stu {
        private String age;
        private String name;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
        super.executeParse(bpmnParse, userTask);
        if (!flag) {
            return;
        }

        /**
         * 解析前保存流程定义
         */
        TaskNodeMapper taskNodeMapper = ApplicationContextHolder.getBean(TaskNodeMapper.class);

        TaskNodePO nodePO = getCurrentTaskNode(userTask.getId(), bpmnParse.getCurrentProcessDefinition().getKey(), userTask);

        Iterator<Map.Entry<String, List<ExtensionElement>>> entryIterator = userTask.getExtensionElements().entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, List<ExtensionElement>> next = entryIterator.next();

            String key = next.getKey();
            List<ExtensionElement> value = next.getValue();

            if (key.equals("config")) {
                ConfigServiceTaskParseHandler.Stu stu = JsonUtils.deserialize(value.get(0)
                                                                                      .getElementText(), ConfigServiceTaskParseHandler.Stu.class);
                System.out.println(stu);

                // todo 保存节点的配置
                taskNodeMapper.updateById(nodePO);
            }
        }
    }

    public ExtensionElement parseUserTaskOperations(BpmnParse bpmnParse, UserTask userTask) {
        //获取扩展属性标签元素
        List<ExtensionElement> tasks = userTask.getExtensionElements()
                .get("userTask");
        if (CollectionUtils.isEmpty(tasks)) {
            return null;
        } else {
            return tasks.get(0);
        }
    }
}
