package com.guhecloud.cube.base.alarm.server.flow.domain.event;

import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 报警移交事件
 *
 * @author lanchun
 * @date 2023/2/28 17:41
 */
@Getter
public class TransferAlarmRecordEvent extends ApplicationEvent {

    private final AlarmStatusEnum previousAlarmStatus;

    private final AlarmRecord alarmRecord;

    public TransferAlarmRecordEvent(Object source, AlarmStatusEnum previousAlarmStatus, AlarmRecord alarmRecord) {
        super(source);
        this.previousAlarmStatus = previousAlarmStatus;
        this.alarmRecord = alarmRecord;
    }
}
