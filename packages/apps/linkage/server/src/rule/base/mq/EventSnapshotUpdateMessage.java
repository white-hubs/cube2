package com.guhecloud.cube.biz.linkage.server.rule.base.mq;

import lombok.Data;

@Data
public class EventSnapshotUpdateMessage {

    public static final String TOPIC = "event_snapshot_update";

    /**
     * 事件id
     */
    private Long eventId;

    /**
     * 变更的快照信息
     */
    private String snapshot;
}
