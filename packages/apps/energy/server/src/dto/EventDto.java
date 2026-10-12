package com.guhecloud.cube.srv.gen.energymanagement.server.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {
    /**
     * 级别主键
     */
    private Long triggerLevelId;

    private String triggerLevelName;

    private Long triggerTypeId;

    private String triggerTypeName;

    private Long triggerTargetKey;

    private String triggerTargetCode;

    private String triggerTargetName;

    private String triggerTargetTypeId;

    private String triggerTargetTypeName;

    private String area;

    private String areaName;

    private String triggerThreshold;

    private String event;

    private String eventName;

    /**
     * 触发时间
     */
    private String triggerTime;

    /**
     * 快照信息
     */
    private String snapshot;
}
