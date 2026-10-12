package com.guhecloud.cube.base.alarm.server.flow.application.process.step;

import com.guhecloud.cube.base.alarm.server.flow.application.process.model.ProcessContext;
import com.guhecloud.cube.base.alarm.server.flow.application.process.model.result.ProcessResult;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.AlarmMessageSender;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class AlarmCreateMessageStep implements AlarmProcessStep {

    private final AlarmMessageSender alarmMessageSender;

    private static final String STEP_NAME = "ALARM_CREATE_MESSAGE_STEP";

    public AlarmCreateMessageStep(AlarmMessageSender alarmMessageSender) {this.alarmMessageSender = alarmMessageSender;}

    @Override
    public ProcessResult execute(ProcessContext context) {
        this.alarmMessageSender.alarmMqMsgSend(context.getAlarmRecord(), context.getAlarmRecord().getAlarmInfo().getAlarmTime());
        return ProcessResult.continued();
    }

    @Override
    public String getName() {
        return STEP_NAME;
    }
}
