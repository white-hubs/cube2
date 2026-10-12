package com.guhecloud.cube.base.alarm.server.flow.application.process.step;

import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordRefDeviceService;
import com.guhecloud.cube.base.alarm.server.flow.application.process.model.ProcessContext;
import com.guhecloud.cube.base.alarm.server.flow.application.process.model.result.ProcessResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class RefDeviceSaveStep implements AlarmProcessStep {

    private final AlarmRecordRefDeviceService alarmRecordRefDeviceService;

    private static final String STEP_NAME = "ALARM_REF_DEVICE_STEP";

    public RefDeviceSaveStep(AlarmRecordRefDeviceService alarmRecordRefDeviceService
                            ) {this.alarmRecordRefDeviceService = alarmRecordRefDeviceService;}

    @Override
    public ProcessResult execute(ProcessContext context) {
        this.alarmRecordRefDeviceService.saveAlarmRefDevice(context.getAlarmRecord().getAlarmInfo().getId(), context.getAlarmRecord()
                .getAlarmTarget()
                .getAlarmTargetKey());
        return ProcessResult.continued();
    }

    @Override
    public String getName() {
        return STEP_NAME;
    }
}
