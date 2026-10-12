package com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo;

import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AlarmWorkCenterRocketMQ {

    public static void sendAlarmStatusChangeTask(AlarmRecord alarmRecord, Long userId, LocalDateTime handlerTime,
                                                 AlarmStatusEnum previousAlarmStatus
                                                ) {
        WorkTaskFlowCirculationMessage workTaskFlowCirculationMessage = WorkTaskFlowCirculationMessage.builder()
                .operationType(WorkTaskFlowOperationEnum.CHANGE)
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
        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, workTaskFlowCirculationMessage, alarmRecord
                .getAlarmInfo().getId().toString());
    }

    public static void sendAlarmInitWork(AlarmRecord alarmRecord) {
        sendAlarmInitWork(alarmRecord, alarmRecord.getAlarmHandlerUsers().stream().map(AlarmHandlerUser::getId)
                .collect(Collectors.toList()));
    }

    public static void sendAlarmInitWork(AlarmRecord alarmRecord, List<Long> userIds) {
        //发送待办初始化消息
        WorkTaskInitMessage workTaskInitMessage = WorkTaskInitMessage
                .builder()
                .operationType(WorkTaskFlowOperationEnum.INSERT)
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
        RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, workTaskInitMessage, alarmRecord.getAlarmInfo().getId()
                .toString());
    }

    public static void sendAlarmHandlerWork(AlarmRecord alarmRecord, Long userId, LocalDateTime handlerTime,
                                            AlarmStatusEnum previousAlarmStatus
                                           ) {
        //发送待办变更消息
        WorkTaskFlowCirculationMessage workTaskFlowCirculationMessage = WorkTaskFlowCirculationMessage.builder()
                .operationType(WorkTaskFlowOperationEnum.CHANGE)
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
        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, workTaskFlowCirculationMessage, alarmRecord
                .getAlarmInfo().getId().toString());
    }
}
