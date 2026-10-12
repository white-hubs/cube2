package com.guhecloud.cube.base.alarm.server.flow.domain.event;

import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 报警催办事件
 *
 * @author lanchun
 * @date 2025/1/11 17:10
 */
@Getter
public class UrgingAlarmRecordEvent extends ApplicationEvent {

    private final AlarmRecord alarmRecord;

    public UrgingAlarmRecordEvent(Object source, AlarmRecord alarmRecord) {
        super(source);
        this.alarmRecord = alarmRecord;
    }
}
