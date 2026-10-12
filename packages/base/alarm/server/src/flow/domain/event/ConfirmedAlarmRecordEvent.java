package com.guhecloud.cube.base.alarm.server.flow.domain.event;

import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 报警确认事件
 *
 * @author lanchun
 * @date 2023/2/28 17:30
 */
@Getter
public class ConfirmedAlarmRecordEvent extends ApplicationEvent {

    private final AlarmRecord alarmRecord;

    public ConfirmedAlarmRecordEvent(Object source, AlarmRecord alarmRecord) {
        super(source);
        this.alarmRecord = alarmRecord;
    }
}
