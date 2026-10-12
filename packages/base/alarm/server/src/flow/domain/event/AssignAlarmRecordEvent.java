package com.guhecloud.cube.base.alarm.server.flow.domain.event;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.AssignAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 报警指派事件
 *
 * @author lanchun
 * @date 2023/2/28 17:43
 */
@Getter
public class AssignAlarmRecordEvent extends ApplicationEvent {

    private final AlarmStatusEnum previousAlarmStatus;

    private final AlarmRecord alarmRecord;

    private final AssignAlarmRecordCommand assignAlarmRecordCommand;

    private final List<AlarmHandlerUser> alarmHandlerUsers;

    public AssignAlarmRecordEvent(Object source, AlarmStatusEnum previousAlarmStatus, AlarmRecord alarmRecord,
                                  AssignAlarmRecordCommand assignAlarmRecordCommand, List<AlarmHandlerUser> alarmHandlerUsers
                                 ) {
        super(source);
        this.previousAlarmStatus = previousAlarmStatus;
        this.alarmRecord = alarmRecord;
        this.assignAlarmRecordCommand = assignAlarmRecordCommand;
        this.alarmHandlerUsers = alarmHandlerUsers;
    }
}
