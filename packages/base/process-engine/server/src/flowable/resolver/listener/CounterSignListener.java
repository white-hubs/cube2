package com.guhecloud.cube.base.flow.flowable.resolver.listener;

import base.constant.VariableConstant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.flowable.common.util.ApproveUserParser;
import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.Properties;
import com.guhecloud.cube.base.flow.flowable.resolver.enumeration.AssigneeTypeEnums;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessExecuteContext;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessInfo;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/11/9 16:44
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CounterSignListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        long currentTimeMillis = System.currentTimeMillis();
        String currentActivityId = execution.getCurrentActivityId();
        List<String> assigneeList = new ArrayList<>();
        String variableKey = currentActivityId + VariableConstant.ASSIGNEE_List;
        String eventName = execution.getEventName();
        Object variable = execution.getVariable(variableKey);
        if ("start".equals(eventName) && Objects.isNull(variable)) {
            ProcessInfo processInfo = ProcessExecuteContext.getProcessInfo(execution.getProcessInstanceId());
            Properties props = ExtProcessDefinition.getChildNode(processInfo.getChildNode(), currentActivityId).getProps();
            String assignedType = props.getAssignedType();
            Map<String, Object> nobody = props.getNobody();
            String methName = AssigneeTypeEnums.valueOf(assignedType).getMethName();
            log.info("当前节点id: {}, 当前节点类型: {}, 当前节点审批人解析方法: {}", currentActivityId, assignedType, methName);
            if (StringUtils.isNotBlank(methName)) {
                try {
                    Object invoke = ReflectionUtils
                            .findMethod(ApproveUserParser.class, methName, DelegateExecution.class, props.getClass(), Long.class)
                            .invoke(ApplicationContextHolder.getBean(ApproveUserParser.class), execution, props, processInfo
                                    .getStartUserInfo().getStartUserId());
                    assigneeList = JsonUtils.deserialize(invoke, new TypeReference<List<String>>() {});
                } catch (Exception e) {
                    throw new RuntimeException("查找审批人失败,请联系管理员重试", e);
                }
            }
            List<String> temporaryVariable = (List<String>) execution.getVariable(VariableConstant.TEMPORARY + variableKey);
            if (CollectionUtils.isNotEmpty(temporaryVariable)) {
                assigneeList.addAll(temporaryVariable);
            }
            if (CollectionUtils.isEmpty(assigneeList)) {
                String handler = String.valueOf(nobody.get("handler"));
                if ("TO_PASS".equals(handler)) {
                    assigneeList.add("100000");
                    execution.setVariable(variableKey, assigneeList);
                } else if ("TO_REFUSE".equals(handler)) {
                    execution.setVariable("autoRefuse", Boolean.TRUE);
                    assigneeList.add("100000");
                    execution.setVariable(variableKey, assigneeList);
                } else if ("TO_ADMIN".equals(handler)) {
                    assigneeList.add("381496");
                    execution.setVariable(variableKey, assigneeList);
                } else if ("TO_USER".equals(handler)) {
                    Object assignedUserObj = nobody.get("assignedUser");
                    if (assignedUserObj != null) {
                        List<Map<String, Object>> assignedUser = (List<Map<String, Object>>) assignedUserObj;
                        if (assignedUser.size() > 0) {
                            for (Map<String, Object> object : assignedUser) {
                                assigneeList.add(String.valueOf(object.get("id")));
                            }
                            execution.setVariable(variableKey, assigneeList);
                        } else {
                            assigneeList.add("100000");
                            execution.setVariable(variableKey, assigneeList);
                        }

                    }

                } else {
                    throw new ServiceException("找不到审批人,请检查配置!!!");
                }
            } else {
                execution.setVariable(variableKey, assigneeList);
            }
        }
        log.info("CounterSignListener 节点审批人设置完成, 节点id: {}, 节点审批人: {}, 耗时: {}", currentActivityId, assigneeList,
                 System.currentTimeMillis() - currentTimeMillis);
    }
}

