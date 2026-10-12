package com.guhecloud.cube.biz.linkage.server.record.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件快照
 */
@Getter
@AllArgsConstructor
public class EventSnapshot {

    /**
     * iot快照
     */
    private String snapshot;

    public void changeAlarmSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }
}
