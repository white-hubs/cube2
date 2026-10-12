package com.guhecloud.cube.base.flow.flowable.common.resovler;

import base.constant.VariableConstant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.flowable.common.util.ApproveUserParser;
import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;
import com.guhecloud.cube.base.flow.flowable.definition.domain.repository.ProcessDefinitionRepository;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.Properties;
import com.guhecloud.cube.base.flow.flowable.resolver.enumeration.AssigneeTypeEnums;
import com.guhecloud.cube.base.flow.flowable.variable.mapper.HistoricVariableMapper;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricVariableInstance;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("nodeUserParser")
@Slf4j
public class NodeUserParser {
    @Resource
    private HistoryService historyService;
    @Resource
    private HistoricVariableMapper historicVariableMapper;

    public List<Long> parse(String activityId, DelegateExecution execution, Long startUserId) {
        List<String> assigneeList = new ArrayList<>();
        String variable = activityId + VariableConstant.ASSIGNEE_List;
        List usersValue = new ArrayList<>();

        if (VariableConstant.ROOT.equals(activityId)) {
            HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                    .variableName(VariableConstant.ROOT).processInstanceId(execution.getProcessInstanceId()).executionId(execution.getId())
                    .singleResult();
            usersValue.add(historicVariableInstance.getValue());
        } else {
            HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                    .variableName(variable).processInstanceId(execution.getProcessInstanceId()).executionId(execution.getId())
                    .singleResult();
            if (Objects.nonNull(historicVariableInstance)) {
                usersValue = (List) historicVariableInstance.getValue();
            }
        }

        if (CollectionUtils.isEmpty(usersValue)) {
            ProcessDefinitionRepository processDefinitionRepository = ApplicationContextHolder.getBean(ProcessDefinitionRepository.class);
            ExtProcessDefinition processDefinition = processDefinitionRepository.getById(execution.getProcessDefinitionId());
            ChildNode currentNode = processDefinition.getCurrentNode(activityId);
            Assert.isNotFound(currentNode, "查找审批人失败,请联系管理员重试");

            Properties props = currentNode.getProps();
            String assignedType = props.getAssignedType();
            Map<String, Object> nobody = props.getNobody();
            String value = historicVariableMapper.selectProcessVariable("temporary" + variable, execution.getProcessInstanceId());
            if (Objects.nonNull(value)) {
                assigneeList.addAll(JsonUtils.deserializeToList(value, String.class));
            }
            String methName = AssigneeTypeEnums.valueOf(assignedType).getMethName();
            log.info("解析列表当前节点id: {}, 当前节点类型: {}, 当前节点审批人解析方法: {}", activityId, assignedType, methName);
            if (StringUtils.isNotBlank(methName)) {
                try {
                    Object invoke = ReflectionUtils.findMethod(ApproveUserParser.class, methName, DelegateExecution.class, props.getClass(), Long.class)
                            .invoke(ApplicationContextHolder.getBean(ApproveUserParser.class), execution, props, startUserId);
                    assigneeList.addAll(JsonUtils.deserialize(invoke, new TypeReference<List<String>>() {
                    }));
                } catch (Exception e) {
                    throw new RuntimeException("查找审批人失败,请联系管理员重试", e);
                }
            }

            if (CollectionUtils.isEmpty(assigneeList)) {
                String handler = String.valueOf(nobody.get("handler"));
                if ("TO_PASS".equals(handler)) {
                    assigneeList.add("100000");
                } else if ("TO_REFUSE".equals(handler)) {
                    assigneeList.add("100000");
                } else if ("TO_ADMIN".equals(handler)) {
                    assigneeList.add("381496");
                } else if ("TO_USER".equals(handler)) {
                    Object assignedUserObj = nobody.get("assignedUser");
                    if (assignedUserObj != null) {
                        List<Map<String, Object>> assignedUser = JsonUtils.deserialize(assignedUserObj, new TypeReference<List<Map<String, Object>>>() {});
                        if (CollectionUtils.isNotEmpty(assignedUser)) {
                            for (Map<String, Object> object : assignedUser) {
                                assigneeList.add(String.valueOf(object.get("id")));
                            }
                        } else {
                            assigneeList.add("100000");
                        }

                    }
                }
            }
        } else {
            assigneeList.addAll(usersValue);
        }
        return assigneeList.stream().map(Long::valueOf).distinct().collect(Collectors.toList());
    }

}
