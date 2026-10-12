package com.guhecloud.cube.srv.gen.maintain.server.electron.util;

import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolTaskStateEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElectronTaskTodoUtil {
    private final ApplicationClient applicationClient;
    private static final String applicationCode = "ELECTRONIC_PATROL";

    public void todoInsert(PatrolTask task, PatrolTaskStateEnum status) {
        String[] userIds = task.getBelonger().split(",");
        List<Long> list = Arrays.stream(userIds).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        this.todoInsert(task, list, status);
    }

    public void todoInsert(PatrolTask task, List<Long> userIds, PatrolTaskStateEnum status) {
        if (ObjectUtils.isEmpty(userIds)) {
            return;
        }
        log.info("巡更任务新增代办任务：{}", task.getStatus());
        //获取应用
        ApplicationDTO application = applicationClient.getApplicationByCode(applicationCode);
        //创建消息体
        TodoTaskInitMessage message = new TodoTaskInitMessage();
        message.setApplicationCode(application.getCode());
        message.setApplicationName(application.getName());
        message.setBusinessKey(String.valueOf(task.getId()));
        message.setBusinessType("ELECTRON_TASK");
        message.setBusinessTypeName("巡更任务");
        message.setBusinessStatus(status.name());
        message.setBusinessStatusName(status.getRemarks());
        message.setTaskName(task.getTaskName());
        message.setTaskTime(LocalDateTime.now());
        message.setOperationType(TodoTaskFlowOperationEnum.INSERT);
        message.setUserIds(userIds);
        //发消息
        RocketMQContextHolder.syncSendOrderly(TodoTaskInitMessage.TOPIC, message, String.valueOf(task.getId()));

    }

    /**
     * 完成代办
     */
    public void todoComplete(PatrolTask task, Set<Long> userIds, String status) {
        log.info("电子巡更修改代办任务状态：{}", task.getStatus());
        TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
        message.setBusinessKey(String.valueOf(task.getId()));
        message.setBusinessStatus(status);
        message.setUserIds(userIds);
        message.setHandlerTime(LocalDateTime.now());
        message.setTaskTime(LocalDateTime.now());
        message.setGeneratedNextStatusTask(false);
        message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
        message.setNextBusinessStatus(PatrolTaskStateEnum.FINISHED.name());
        message.setNextBusinessStatusName(PatrolTaskStateEnum.FINISHED.getRemarks());
        //发消息
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, message, String.valueOf(task.getId()));
    }

    /**
     * 重新发送代办任务
     */
    public void todoRepeat(PatrolTask task, List<Long> userIds) {
        TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
        message.setOperationType(TodoTaskFlowOperationEnum.DELETE);
        message.setBusinessKey(String.valueOf(task.getId()));
        // 删除
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, message, String.valueOf(task.getId()));
        // 重新发送代办
        this.todoInsert(task, userIds, PatrolTaskStateEnum.IN_EXECUTION);
    }
}
