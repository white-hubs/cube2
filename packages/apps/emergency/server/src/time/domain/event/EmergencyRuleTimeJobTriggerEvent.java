package com.guhecloud.cube.biz.emergency.server.time.domain.event;

import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTask;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class EmergencyRuleTimeJobTriggerEvent extends ApplicationEvent {

    private final TimerTask timerTask;

    private final Long jobLogId;

    public EmergencyRuleTimeJobTriggerEvent(Object source, TimerTask TimerTask, Long jobLogId) {
        super(source);
        this.timerTask = TimerTask;
        this.jobLogId = jobLogId;
    }
}
