package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway;

import com.guhecloud.cube.base.flow.flowable.definition.domain.ExtProcessDefinition;
import com.guhecloud.cube.base.flow.flowable.definition.domain.repository.ProcessDefinitionRepository;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.PreReduceSignCommand;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PreReduceSignChecker {

    private final HistoryService historyService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public void preCheck(PreReduceSignCommand reduceSignCommand) {
        String originalTaskKey = getOriginalTaskKey(reduceSignCommand.getProcessInstanceId());

        // 检查指定审批人是否开启
        checkOpenConfig(reduceSignCommand.getProcessInstanceId(), originalTaskKey);

        //  是否是当前活跃节点校验
        checkActiveNode(reduceSignCommand.getProcessInstanceId(), reduceSignCommand.getTaskKey());

        // 检查人员可用性
        checkAvailable(reduceSignCommand.getProcessInstanceId(), originalTaskKey);
    }

    private void checkOpenConfig(String processInstanceId, String originalTaskKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //  是否开启了指定审批人配置
        ExtProcessDefinition processDefinition = ApplicationContextHolder.getBean(ProcessDefinitionRepository.class)
                .getById(processInstance.getProcessDefinitionId());
        ChildNode currentNode = processDefinition.getCurrentNode(originalTaskKey);
        Boolean designatedApprover = currentNode.getProps().getDesignatedApprover();
        if (Objects.isNull(designatedApprover) || !designatedApprover) {
            throw new ServiceException("指定审批人未开启");
        }
        if (StringUtils.isEmpty(originalTaskKey)) {
            throw new ServiceException("没有获取到当前活跃节点");
        }
    }

    private String getOriginalTaskKey(String processInstanceId) {
        List<Task> list = taskService.createTaskQuery().active().processInstanceId(processInstanceId).list();
        String originalTaskKey = "";
        if (CollectionUtils.isNotEmpty(list)) {
            Task task = list.get(0);
            originalTaskKey = task.getTaskDefinitionKey();
        }
        return originalTaskKey;
    }

    private void checkActiveNode(String processInstanceId, String taskKey) {
        //  是否是当前活跃节点校验
        long activedTaskCount = taskService.createTaskQuery().active().processInstanceId(processInstanceId).taskDefinitionKey(taskKey)
                .count();
        if (activedTaskCount != 0) {
            throw new ServiceException("当前节点不能指定审批人");
        }
    }

    private void checkAvailable(String processInstanceId, String originalTaskKey) {
        // 只有当前节点的人员才能加签
        List<HistoricTaskInstance> historicTaskInstances = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .unfinished()
                .taskDefinitionKey(originalTaskKey)
                .list();
        Set<Long> available = historicTaskInstances.stream().map(historicTaskInstance -> Long.parseLong(historicTaskInstance.getAssignee()))
                .collect(Collectors.toSet());

        Long userId = SecurityContextHolder.getPrincipal().getId();
        if (!available.contains(userId)) {
            throw new ServiceException("当前人员不能指定审批人");
        }
    }
}
