package com.guhecloud.cube.base.alarm.server.flow.application.event.handler;

import com.guhecloud.cube.base.alarm.client.message.AlarmRecordAssignedMessage;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordOperatorLogService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordTargetService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.IncreaseAlarmRecordTargetCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.process.manager.AlarmProcessManager;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.IncreaseAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.InitCreateAssignAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.factory.AlarmRecordFactory;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.AlarmMessageSender;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo.AlarmTodoRocketMQ;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo.AlarmWorkCenterRocketMQ;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.AlarmRecordRepository;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncreaseAlarmRecordEventHandler {

    private final AlarmRecordOperatorLogService alarmRecordOperatorLogService;

    private final AlarmRecordTargetService alarmRecordTargetService;

    private final AlarmMessageSender alarmMessageSender;

    private final AlarmProcessManager alarmProcessManager;

    private final AlarmRecordRepository alarmRecordRepository;

    @EventListener
    public void processAlarmStep(final IncreaseAlarmRecordEvent increaseAlarmRecordEvent) {
        alarmProcessManager.executeProcess(increaseAlarmRecordEvent.getAlarmRecord(), SessionContextHolder.getRequester().getTenant());
    }

    @EventListener
    public void initAssignAction(final InitCreateAssignAlarmRecordEvent event) {
        if (Objects.isNull(event.getAlarmRecord()) || CollectionUtils.isEmpty(event.getHandlerUsers())) {
            return;
        }
        if (event.getAlarmRecord().getAlarmStatus().getAlarmStatus() == AlarmStatusEnum.ALARM_HANDLER) {
            return;
        }
        var alarmRecord = event.getAlarmRecord();
        alarmRecord.changeAlarmHandlerUsers(AlarmRecordFactory.buildAlarmHandlerUsers(event.getHandlerUsers()));
        var operatorLogContent =
                "系统发生报警节点:" + "《" + event.getAlarmRecord().getAlarmInfo().getAlarmEventName() + "%s》";
        var handlerUsers = "";
        if (!CollectionUtils.isEmpty(alarmRecord.getAlarmHandlerUsers())) {
            handlerUsers = ",系统自动将报警事件指派给了" +
                           alarmRecord.getAlarmHandlerUsers().stream()
                                   .map(AlarmHandlerUser::getUserName).collect(Collectors.joining(","));
        }
        if (!CollectionUtils.isEmpty(event.getHandlerUsers())) {
            //记录报警通知对象信息
            this.alarmRecordTargetService.saveBatch(alarmRecord.getAlarmInfo()
                                                            .getId(), DateUtils.now(),
                                                    AlarmRecordFactory.buildAlarmNotifyUsers(event.getHandlerUsers()));
            alarmRecordRepository.saveHandlerUsers(alarmRecord);
            sendAlarmRecordAssignedMessage(alarmRecord, event.getHandlerUsers());
        }
        operatorLogContent = String.format(operatorLogContent, handlerUsers);
        //新增报警初始化日志信息
        this.alarmRecordOperatorLogService.save(alarmRecord.getAlarmInfo().getId(),
                                                AlarmOperatorTypeEnum.ALARM_OCCURRENCE,
                                                null,
                                                "系统",
                                                alarmRecord.getAlarmInfo()
                                                        .getAlarmTime(), operatorLogContent, null);
        if (!CollectionUtils.isEmpty(alarmRecord.getAlarmHandlerUsers())) {
            //发送待办初始化消息
            AlarmTodoRocketMQ.sendAlarmInitTodo(alarmRecord);
            AlarmWorkCenterRocketMQ.sendAlarmInitWork(alarmRecord);
            //发送通知
            this.alarmMessageSender.alarmAssignSend(alarmRecord,
                                                    alarmRecord.getAlarmHandlerUsers()
                                                            .stream().map(AlarmHandlerUser::getId)
                                                            .collect(Collectors.toSet()));
        }
        //广播通知
        this.alarmMessageSender.alarmBroadcastSend(event.getAlarmRecord());
    }

    /**
     * 发送报警初始化指派完成消息，用于依赖报警处理/通知人的后续联动。
     */
    private void sendAlarmRecordAssignedMessage(AlarmRecord alarmRecord, List<IncreaseAlarmRecordTargetCommand> handlerUsers) {
        try {
            var userMessages = handlerUsers.stream()
                    .map(user -> AlarmRecordAssignedMessage.UserMessage.builder()
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .departmentId(user.getDepartmentId())
                            .departmentName(user.getDepartmentName())
                            .build())
                    .toList();
            AlarmRecordAssignedMessage message = AlarmRecordAssignedMessage.builder()
                    .tenant(SessionContextHolder.getRequester().getTenant())
                    .alarmRecordId(alarmRecord.getAlarmInfo().getId())
                    .code(alarmRecord.getAlarmInfo().getCode())
                    .applicationCode(alarmRecord.getAlarmType().getApplicationCode())
                    .applicationName(alarmRecord.getAlarmType().getApplicationName())
                    .handlerUsers(userMessages)
                    .notifyUsers(userMessages)
                    .build();
            RocketMQContextHolder.send(AlarmRecordAssignedMessage.TOPIC, message);
        } catch (Exception e) {
            log.warn("报警初始化指派完成消息发送失败 alarmRecordId={}", alarmRecord.getAlarmInfo().getId(), e);
        }
    }
}
