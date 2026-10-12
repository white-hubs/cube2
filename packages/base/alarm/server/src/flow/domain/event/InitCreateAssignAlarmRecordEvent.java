package com.guhecloud.cube.base.alarm.server.flow.domain.event;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.IncreaseAlarmRecordTargetCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 初始化创建时报警指派事件
 */
@Getter
public class InitCreateAssignAlarmRecordEvent extends ApplicationEvent {

    private final AlarmRecord alarmRecord;

    /**
     * 处理用户
     */
    private final List<IncreaseAlarmRecordTargetCommand> handlerUsers;

    /**
     * 报警抄送通知
     */
    private final List<String> channelTypes;

    public InitCreateAssignAlarmRecordEvent(Object source, AlarmRecord alarmRecord, List<IncreaseAlarmRecordTargetCommand> handlerUsers,
                                            List<String> channelTypes
                                           ) {
        super(source);
        this.alarmRecord = alarmRecord;
        this.handlerUsers = handlerUsers;
        this.channelTypes = channelTypes;
    }
}
