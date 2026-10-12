package com.guhecloud.cube.biz.linkage.server.record.domain.aggregates;

import com.guhecloud.cube.biz.linkage.server.record.domain.entity.EventBaseInfo;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventApp;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventDevice;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventLevel;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventSnapshot;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 事件记录
 */
@Getter
@Builder
@AllArgsConstructor
public class EventRecord {

    /**
     * 基本信息
     */
    private EventBaseInfo baseInfo;

    /**
     * 事件级别
     */
    private EventLevel level;

    /**
     * 事件设备
     */
    private EventDevice eventDevice;

    /**
     * 事件类型信息
     */
    private EventType eventType;

    /**
     * 事件应用
     */
    private EventApp eventApp;

    /**
     * 事件快照
     */
    private EventSnapshot eventSnapshot;
}
