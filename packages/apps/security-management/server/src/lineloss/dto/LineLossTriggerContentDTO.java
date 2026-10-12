package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto;

import lombok.Data;

@Data
public class LineLossTriggerContentDTO {
    private Long triggeredRuleId;
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

    private Boolean enabled = true;

    /**
     * 快照信息
     */
    private String snapshot;
}
