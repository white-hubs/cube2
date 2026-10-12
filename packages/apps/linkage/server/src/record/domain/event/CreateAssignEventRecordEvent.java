package com.guhecloud.cube.biz.linkage.server.record.domain.event;

import com.guhecloud.cube.biz.linkage.server.record.domain.aggregates.EventRecord;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 初始化创建事件
 */
@Getter
public class CreateAssignEventRecordEvent extends ApplicationEvent {

    private final EventRecord eventRecord;

    public CreateAssignEventRecordEvent(Object source, EventRecord eventRecord
                                       ) {
        super(source);
        this.eventRecord = eventRecord;
    }
}
