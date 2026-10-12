package com.guhecloud.cube.base.alarm.server.flow.application.event.handler;

import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordOperatorLogService;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.ConfirmedAlarmRecordEvent;
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

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfirmedAlarmRecordEventHandler {

    private final AlarmRecordOperatorLogService alarmRecordOperatorLogService;

    private final UserClient userClient;

    @EventListener
    public void confirmedAlarmRecord(final ConfirmedAlarmRecordEvent confirmedAlarmRecordEvent) {
        UserDTO userDTO = this.userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        String operatorLogContent = "报警确认:" + userDTO.getName() + "确认接收了报警,正在处理中";
        //记录日志消息
        this.alarmRecordOperatorLogService.save(confirmedAlarmRecordEvent.getAlarmRecord().getAlarmInfo()
                                                        .getId(), AlarmOperatorTypeEnum.ALARM_CONFIRMED, SecurityContextHolder
                                                        .getPrincipal().getId()
                                                        .toString(), userDTO.getName(), DateUtils.now(), operatorLogContent, null);
        //发送待办变更消息
        AlarmTodoRocketMQ.sendAlarmStatusChangeTodo(confirmedAlarmRecordEvent.getAlarmRecord(),
                                                    confirmedAlarmRecordEvent.getAlarmRecord().getAlarmConfirmed()
                                                            .getAlarmConfirmedBy(),
                                                    confirmedAlarmRecordEvent.getAlarmRecord().getAlarmConfirmed()
                                                            .getAlarmConfirmedTime(),
                                                    AlarmStatusEnum.ALARM_UN_CONFIRMED
                                                   );
        AlarmWorkCenterRocketMQ.sendAlarmStatusChangeTask(confirmedAlarmRecordEvent.getAlarmRecord(),
                                                          confirmedAlarmRecordEvent.getAlarmRecord().getAlarmConfirmed()
                                                                  .getAlarmConfirmedBy(),
                                                          confirmedAlarmRecordEvent.getAlarmRecord().getAlarmConfirmed()
                                                                  .getAlarmConfirmedTime(),
                                                          AlarmStatusEnum.ALARM_UN_CONFIRMED);
    }
}
