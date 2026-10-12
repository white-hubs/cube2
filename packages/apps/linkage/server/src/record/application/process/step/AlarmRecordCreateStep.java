package com.guhecloud.cube.biz.linkage.server.record.application.process.step;

import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.alarmnotify.handler.LinkageActionAlarmNotifyHandler;
import com.guhecloud.cube.biz.linkage.server.record.application.process.model.ProcessContext;
import com.guhecloud.cube.biz.linkage.server.record.application.process.model.result.ProcessResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Order(2)
@Component
public class AlarmRecordCreateStep implements EventRecordProcessStep {

    private final LinkageActionAlarmNotifyHandler linkageActionAlarmNotifyHandler;

    private final EventService eventService;

    private static final String STEP_NAME = "ALARM_CREATE_STEP";

    public AlarmRecordCreateStep(LinkageActionAlarmNotifyHandler linkageActionAlarmNotifyHandler, EventService eventService
                                ) {
        this.linkageActionAlarmNotifyHandler = linkageActionAlarmNotifyHandler;
        this.eventService = eventService;
    }

    @Override
    public ProcessResult execute(ProcessContext context) {
        var event = eventService.getById(context.getEventRecord().getBaseInfo().getEventId());
        linkageActionAlarmNotifyHandler.sendAlarmMsg(event, Collections.emptyList());
        return ProcessResult.continued();
    }

    @Override
    public String getName() {
        return STEP_NAME;
    }

}
