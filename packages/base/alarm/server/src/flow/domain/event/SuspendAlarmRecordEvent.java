package com.guhecloud.cube.base.alarm.server.flow.domain.event;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.SuspendAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 报警挂起操作事件
 *
 * @author lanchun
 * @date 2023/2/28 17:44
 */
@Getter
public class SuspendAlarmRecordEvent extends ApplicationEvent {

    private final AlarmStatusEnum previousAlarmStatus;

    private final AlarmRecord alarmRecord;

    private final SuspendAlarmRecordCommand suspendAlarmRecordCommand;

    public SuspendAlarmRecordEvent(Object source, AlarmStatusEnum previousAlarmStatus, AlarmRecord alarmRecord,
                                   SuspendAlarmRecordCommand suspendAlarmRecordCommand
                                  ) {
        super(source);
        this.previousAlarmStatus = previousAlarmStatus;
        this.alarmRecord = alarmRecord;
        this.suspendAlarmRecordCommand = suspendAlarmRecordCommand;
    }
}
