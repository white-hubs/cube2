package com.guhecloud.cube.base.alarm.server.flow.application.process.step;

import com.guhecloud.cube.base.alarm.server.flow.application.process.model.ProcessContext;
import com.guhecloud.cube.base.alarm.server.flow.application.process.model.result.ProcessResult;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.AlarmMessageSender;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(0)
@Component
public class AlarmRecordOccurredMessageStep implements AlarmProcessStep {

    private final AlarmMessageSender alarmMessageSender;

    private static final String STEP_NAME = "ALARM_RECORD_OCCURRED_MESSAGE_STEP";

    public AlarmRecordOccurredMessageStep(AlarmMessageSender alarmMessageSender) {
        this.alarmMessageSender = alarmMessageSender;
    }

    @Override
    public ProcessResult execute(ProcessContext context) {
        this.alarmMessageSender.alarmRecordOccurredSend(context.getAlarmRecord());
        return ProcessResult.continued();
    }

    @Override
    public String getName() {
        return STEP_NAME;
    }
}
