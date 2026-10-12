package com.guhecloud.cube.base.alarm.server.flow.application.event.handler;

import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordOperatorLogService;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.UrgingAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.AlarmMessageSender;
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
public class UrgingAlarmRecordEventHandler {

    private final AlarmRecordOperatorLogService alarmRecordOperatorLogService;

    private final UserClient userClient;

    private final AlarmMessageSender alarmMessageSender;

    @EventListener
    public void urgingAlarmRecord(final UrgingAlarmRecordEvent urgingAlarmRecordEvent) {
        UserDTO userDTO = this.userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        String operatorLogContent = userDTO.getName() + "对该事件进行了催办";
        //记录日志消息
        this.alarmRecordOperatorLogService.save(urgingAlarmRecordEvent.getAlarmRecord().getAlarmInfo()
                                                        .getId(), AlarmOperatorTypeEnum.ALARM_URGING, SecurityContextHolder
                                                        .getPrincipal().getId()
                                                        .toString(), userDTO.getName(), DateUtils.now(), operatorLogContent, null);
        //发送报警通知
        this.alarmMessageSender.alarmAssignSend(urgingAlarmRecordEvent.getAlarmRecord(),
                                                urgingAlarmRecordEvent.getAlarmRecord().getAlarmHandlerUsers()
                                                        .stream().map(AlarmHandlerUser::getId)
                                                        .collect(Collectors.toSet()));
    }
}
