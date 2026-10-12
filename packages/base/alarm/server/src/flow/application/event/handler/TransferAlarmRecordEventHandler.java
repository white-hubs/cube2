package com.guhecloud.cube.base.alarm.server.flow.application.event.handler;

import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordOperatorLogService;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.TransferAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.AlarmMessageSender;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo.AlarmTodoRocketMQ;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo.AlarmWorkCenterRocketMQ;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransferAlarmRecordEventHandler {

    private final AlarmRecordOperatorLogService alarmRecordOperatorLogService;

    private final UserClient userClient;

    private final AlarmMessageSender alarmMessageSender;

    @EventListener
    public void transferAlarmRecord(final TransferAlarmRecordEvent transferAlarmRecordEvent) {
        UserDTO userDTO = this.userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        //日志格式
        String operatorLogContent = "移交节点:" + userDTO.getName() + "将报警任务移交给了" +
                                    transferAlarmRecordEvent.getAlarmRecord().getAlarmHandlerUsers().get(0).getUserName();
        //记录日志消息
        this.alarmRecordOperatorLogService.save(transferAlarmRecordEvent.getAlarmRecord().getAlarmInfo()
                                                        .getId(), AlarmOperatorTypeEnum.ALARM_TRANSFER, SecurityContextHolder
                                                        .getPrincipal().getId().toString(), SecurityContextHolder.getPrincipal()
                                                        .getName(), DateUtils.now(), operatorLogContent, null);
        //发送待办变更消息
        AlarmTodoRocketMQ.sendAlarmStatusChangeTodo(transferAlarmRecordEvent.getAlarmRecord(),
                                                    transferAlarmRecordEvent.getAlarmRecord().getAlarmConfirmed()
                                                            .getAlarmConfirmedBy(),
                                                    DateUtils.now(),
                                                    transferAlarmRecordEvent.getPreviousAlarmStatus()
                                                   );
        AlarmWorkCenterRocketMQ.sendAlarmStatusChangeTask(transferAlarmRecordEvent.getAlarmRecord(),
                                                          transferAlarmRecordEvent.getAlarmRecord().getAlarmConfirmed()
                                                                  .getAlarmConfirmedBy(),
                                                          DateUtils.now(),
                                                          transferAlarmRecordEvent.getPreviousAlarmStatus()
                                                         );
        //发送通知
        this.alarmMessageSender.alarmAssignSend(transferAlarmRecordEvent.getAlarmRecord(),
                                                transferAlarmRecordEvent.getAlarmRecord().getAlarmHandlerUsers()
                                                        .stream().map(AlarmHandlerUser::getId)
                                                        .collect(Collectors.toSet()));
    }
}
