package com.guhecloud.cube.biz.linkage.server.record.application.process.event;

import com.guhecloud.cube.biz.linkage.server.record.domain.aggregates.EventRecord;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JudgmentTimeoutEvent extends ApplicationEvent {

    private final EventRecord eventRecord;

    public JudgmentTimeoutEvent(Object source, EventRecord eventRecord) {
        super(source);
        this.eventRecord = eventRecord;
    }
}
