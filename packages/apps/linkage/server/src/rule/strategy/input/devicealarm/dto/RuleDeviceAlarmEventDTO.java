package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 设备报警事件
 *
 * @author lanchun
 * @date 2022/6/28 9:56
 */
@Data
@Builder
public class RuleDeviceAlarmEventDTO {

    /**
     * 级别主键
     */
    private Long triggerLevelId;

    /**
     * 级别名称
     */
    private String triggerLevelName;

    private Long triggerTypeId;

    /**
     * 类型名称
     */
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
