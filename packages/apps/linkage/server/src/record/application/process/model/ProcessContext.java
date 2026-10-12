package com.guhecloud.cube.biz.linkage.server.record.application.process.model;

import com.guhecloud.cube.biz.linkage.server.record.domain.aggregates.EventRecord;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ProcessContext {

    private final EventRecord eventRecord;

    private final Integer tenant;

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    public ProcessContext(EventRecord eventRecord, Integer tenant) {
        this.eventRecord = eventRecord;
        this.tenant = tenant;
    }
}
