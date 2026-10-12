package com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo;

import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AlarmTodoRocketMQ {

    public static void sendAlarmStatusChangeTodo(AlarmRecord alarmRecord, Long userId, LocalDateTime handlerTime,
                                                 AlarmStatusEnum previousAlarmStatus
                                                ) {
        //发送待办变更消息
        TodoTaskFlowCirculationMessage todoTaskFlowCirculationMessage = TodoTaskFlowCirculationMessage.builder()
                .operationType(TodoTaskFlowOperationEnum.CHANGE)
                .businessKey(alarmRecord
                                     .getAlarmInfo()
                                     .getId()
                                     .toString())
                .userId(userId)
                .handlerTime(handlerTime)
                .businessStatus(previousAlarmStatus.name())
                .generatedNextStatusTask(true)
                .taskTime(DateUtils.now())
                .nextBusinessStatus(alarmRecord
                                            .getAlarmStatus()
                                            .getAlarmStatus()
                                            .name())
                .nextBusinessStatusName(alarmRecord
                                                .getAlarmStatus()
                                                .getAlarmStatus()
                                                .getDescription())
                .nextUserIds(alarmRecord
                                     .getAlarmHandlerUsers()
                                     .stream()
                                     .map(AlarmHandlerUser::getId)
                                     .collect(Collectors.toList()))
                .build();
        //顺序发送
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC, todoTaskFlowCirculationMessage, alarmRecord
                .getAlarmInfo().getId().toString());
    }

    public static void sendAlarmInitTodo(AlarmRecord alarmRecord) {
        sendAlarmInitTodo(alarmRecord, alarmRecord.getAlarmHandlerUsers().stream().map(AlarmHandlerUser::getId)
                .collect(Collectors.toList()));
    }

    public static void sendAlarmInitTodo(AlarmRecord alarmRecord, List<Long> userIds) {
        //发送待办初始化消息
        TodoTaskInitMessage todoTaskInitMessage = TodoTaskInitMessage
                .builder()
                .operationType(TodoTaskFlowOperationEnum.INSERT)
                .businessKey(alarmRecord.getAlarmInfo().getId().toString())
                .businessType(alarmRecord.getAlarmType().getCode())
                .businessTypeName(alarmRecord.getAlarmType().getName())
                .taskName(alarmRecord.getAlarmInfo().getAlarmEventName())
                .taskTime(alarmRecord.getAlarmInfo().getAlarmTime())
                .applicationCode(alarmRecord.getAlarmType().getApplicationCode())
                .applicationName(alarmRecord.getAlarmType().getApplicationName())
                .businessStatus(alarmRecord.getAlarmStatus().getAlarmStatus().name())
                .businessStatusName(alarmRecord.getAlarmStatus().getAlarmStatus().getDescription())
                .userIds(userIds)
                .build();
        //顺序发送
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC, todoTaskInitMessage, alarmRecord.getAlarmInfo().getId()
                .toString());
    }

    public static void sendAlarmHandlerTodo(AlarmRecord alarmRecord, Long userId, LocalDateTime handlerTime,
                                            AlarmStatusEnum previousAlarmStatus
                                           ) {
        //发送待办变更消息
        TodoTaskFlowCirculationMessage todoTaskFlowCirculationMessage = TodoTaskFlowCirculationMessage.builder()
                .operationType(TodoTaskFlowOperationEnum.CHANGE)
                .businessKey(alarmRecord
                                     .getAlarmInfo()
                                     .getId()
                                     .toString())
                .handlerTime(handlerTime)
                //完成节点状态
                .businessStatus(previousAlarmStatus.name())
                .userId(userId)
                .generatedNextStatusTask(false)
                .taskTime(DateUtils.now())
                .nextBusinessStatus(alarmRecord
                                            .getAlarmStatus()
                                            .getAlarmStatus()
                                            .name())
                .nextBusinessStatusName(alarmRecord
                                                .getAlarmStatus()
                                                .getAlarmStatus()
                                                .getDescription())
                .build();
        //顺序发送
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowMessage.TOPIC, todoTaskFlowCirculationMessage, alarmRecord
                .getAlarmInfo().getId().toString());
    }
}
