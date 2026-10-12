package com.guhecloud.cube.base.alarm.server.flow.application.process.model;

import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ProcessContext {

    private final AlarmRecord alarmRecord;

    private final Integer tenant;

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    public ProcessContext(AlarmRecord alarmRecord, Integer tenant) {
        this.alarmRecord = alarmRecord;
        this.tenant = tenant;
    }
}
