package com.guhecloud.cube.base.flow.flowable.task.application.event.publish;

import com.guhecloud.cube.base.flow.flowable.task.domain.valueobject.AutoPassTodoMessage;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskDelMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: 570
 * @createTime: 2023-07-01 23:20
 * @description: 删除任务代办消息
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeleteTaskTodoMessage {

    private final HistoryService historyService;

    public void deleteOtherCountersignTask(AutoPassTodoMessage message) {
        try {
            List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                    .taskDefinitionKey(message.getTaskDefinitionKey())
                    .processInstanceId(message.getProcessInstanceId())
                    .list().stream().filter(task -> !StringUtils.equals(task.getId(), message.getId())).collect(Collectors.toList());
            log.info("待删除的待办任务:id{},nodeKey:{}"
                    , JsonUtils.serialize(taskList.stream().map(TaskInfo::getId).collect(Collectors.toList()))
                    , JsonUtils.serialize(taskList.stream().map(TaskInfo::getTaskDefinitionKey).collect(Collectors.toList())));
            taskList.forEach(task -> {
                TodoTaskDelMessage todoTaskDelMessage = new TodoTaskDelMessage();
                todoTaskDelMessage.setBusinessKey(task.getId());
                todoTaskDelMessage.setOperationType(TodoTaskFlowOperationEnum.DELETE);
                RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC, todoTaskDelMessage, message.getProcessInstanceId());
            });
        } catch (Exception e) {
            log.error("发送会签待办删除消息异常:{}", e);
        }
    }
}
