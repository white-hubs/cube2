package com.guhecloud.cube.base.alarm.server.flow.domain.event;

import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class RemoveAlarmRecordEvent extends ApplicationEvent {

    private final List<AlarmRecordPO> removeAlarmRecords;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public RemoveAlarmRecordEvent(Object source, List<AlarmRecordPO> removeAlarmRecords) {
        super(source);
        this.removeAlarmRecords = removeAlarmRecords;
    }
}
