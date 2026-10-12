package com.guhecloud.cube.base.flow.flowable.definition.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/8/29 14:59
 * @description
 */
@Setter
@Getter
@AllArgsConstructor
public class ExtProcessDefinition {

    private String key;

    private String formKey;

    private Process mainProcess;

    private ProcessDefinition processDefinition;

    // 流程定义配置
    public ExtProcessDefinition(String key) {
        this.key = key;
    }

    public ExtProcessDefinition(String key, String formKey) {
        this.key = key;
        this.formKey = formKey;
    }

    public ChildNode getCurrentNode(String currentActivityId) {
        String processJson = mainProcess.getAttributeValue(ProcessConstant.NAMESPACE, ProcessConstant.PROCESS_FORM_JSON);
        return getChildNode(JsonUtils.deserialize(processJson, new TypeReference<ChildNode>() {}), currentActivityId);
    }

    public static ChildNode getChildNode(ChildNode childNode, String nodeId) {
        Map<String, ChildNode> childNodeMap = new HashMap<>();
        if (StringUtils.isNotBlank(childNode.getId())) {
            getChildNode(childNode, childNodeMap);
        }

        Set<String> set = childNodeMap.keySet();
        for (String s : set) {
            if (StringUtils.isNotBlank(s)) {
                if (s.equals(nodeId)) {
                    return childNodeMap.get(s);
                }
            }
        }
        return new ChildNode();
    }

    private static void getChildNode(ChildNode childNode, Map<String, ChildNode> childNodeMap) {
        childNodeMap.put(childNode.getId(), childNode);
        List<ChildNode> branchs = childNode.getBranchs();
        ChildNode children = childNode.getChildren();
        if (branchs != null && branchs.size() > 0) {
            for (ChildNode branch : branchs) {
                if (StringUtils.isNotBlank(branch.getId())) {
                    childNodeMap.put(branch.getId(), branch);
                    getChildNode(branch, childNodeMap);
                }
            }
        }

        if (children != null) {
            childNodeMap.put(children.getId(), children);
            getChildNode(children, childNodeMap);
        }

    }
}
