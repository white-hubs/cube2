package com.guhecloud.cube.srv.gen.property.management.server.property.util;

import com.google.common.collect.Sets;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTask;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyTaskTodoUtil {
    private final ApplicationClient applicationClient;
    private static final String applicationCode = "PROPERTY_MANAGEMENT";

    public void todoInsert(PropertyCheckTask task, String businessType, String businessTypeName) {
        log.info("{}:新增代办任务：{}", businessType, task.getStatus());
        //获取应用
        ApplicationDTO application = applicationClient.getApplicationByCode(applicationCode);
        List<Long> list = new ArrayList<>();
        list.add(task.getCheckUserId());
        //创建消息体
        TodoTaskInitMessage message = new TodoTaskInitMessage();
        message.setApplicationCode(application.getCode());
        message.setApplicationName(application.getName());
        message.setBusinessKey(String.valueOf(task.getId()));
        message.setBusinessType(businessType);
        message.setBusinessTypeName(businessTypeName);
        message.setBusinessStatus(task.getStatus());
        message.setBusinessStatusName(TaskStatusEnum.getTaskStatusName(task.getStatus()));
        message.setTaskName(task.getPlanName());
        message.setTaskTime(DateUtils.now());
        message.setOperationType(TodoTaskFlowOperationEnum.INSERT);
        message.setUserIds(list);
        //发消息
        RocketMQContextHolder.syncSendOrderly(TodoTaskInitMessage.TOPIC, message, String.valueOf(task.getId()));
    }

    /**
     * 发送代办消息
     */
    public void todo(PropertyCheckTask task, String businessType) {
        log.info("{}:修改代办任务状态：{}", businessType, task.getStatus());
        TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
        message.setBusinessKey(String.valueOf(task.getId()));
        message.setBusinessStatus(TaskStatusEnum.UNDONE.name());
        message.setUserIds(Sets.newHashSet(task.getCheckUserId()));
        message.setHandlerTime(DateUtils.now());
        message.setTaskTime(DateUtils.now());
        message.setGeneratedNextStatusTask(false);
        message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
        message.setNextBusinessStatus(task.getStatus());
        message.setNextBusinessStatusName(TaskStatusEnum.getTaskStatusName(task.getStatus()));
        //发消息
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, message, String.valueOf(task.getId()));
    }
}
