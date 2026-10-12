package com.guhecloud.cube.base.alarm.server.flow.domain.event;

import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 创建报警事件
 *
 * @author lanchun
 * @date 2023/2/28 17:16
 */
@Getter
public class IncreaseAlarmRecordEvent extends ApplicationEvent {

    private final AlarmRecord alarmRecord;

    public IncreaseAlarmRecordEvent(Object source, AlarmRecord alarmRecord) {
        super(source);
        this.alarmRecord = alarmRecord;
    }
}
