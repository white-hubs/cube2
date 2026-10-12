package com.guhecloud.cube.base.alarm.server.flow.domain.event;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.HandlerAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 报警处理事件
 *
 * @author lanchun
 * @date 2023/2/28 17:44
 */
@Getter
public class HandlerAlarmRecordEvent extends ApplicationEvent {

    private final AlarmStatusEnum previousAlarmStatus;

    private final AlarmRecord alarmRecord;

    private final HandlerAlarmRecordCommand handlerAlarmRecordCommand;

    public HandlerAlarmRecordEvent(Object source, AlarmStatusEnum previousAlarmStatus, AlarmRecord alarmRecord,
                                   HandlerAlarmRecordCommand handlerAlarmRecordCommand
                                  ) {
        super(source);
        this.previousAlarmStatus = previousAlarmStatus;
        this.alarmRecord = alarmRecord;
        this.handlerAlarmRecordCommand = handlerAlarmRecordCommand;
    }
}
