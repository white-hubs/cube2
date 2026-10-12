package com.guhecloud.cube.biz.linkage.server.record.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRecordVO {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 报警事件
     */
    private String name;

    /**
     * 事件id
     */
    private Long eventId;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

    /**
     * 事件设备
     */
    private EventDeviceVO eventDevice;

    /**
     * 事件级别
     */
    private EventLevelVO eventLevel;

    /**
     * 事件快照
     */
    private EventSnapshotVO eventSnapshot;

    /**
     * 事件应用
     */
    private EventAppVO eventApp;

    /**
     * 事件分类
     */
    private EventTypeVO eventType;

    /**
     * 辅助识别结果
     */
    private EventAssistConfirmVO assistConfirmResult;
}
