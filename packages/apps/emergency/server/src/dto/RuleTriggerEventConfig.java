package com.guhecloud.cube.biz.emergency.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预案规则触发事件信息
 *
 * @author lanchun
 * @date 2022/11/2 10:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTriggerEventConfig {

    /**
     * 触发对象主键
     */
    private Long triggerTargetKey;

    /**
     * 触发对象编号
     */
    private String triggerTargetCode;

    /**
     * 触发对象名称
     */
    private String triggerTargetName;

    /**
     * 触发对象类型主键
     */
    private String triggerTargetTypeId;

    /**
     * 触发对象类型名称
     */
    private String triggerTargetTypeName;

    /**
     * 报警对象经纬度
     */
    private String triggerTargetLongitude;

    /**
     * 对象位置信息
     */
    private String triggerTargetAddress;

    /**
     * 触发对象名称
     */
    private String triggerTypeName;

    /**
     * 触发级别名称
     */
    private String triggerLevelName;

    /**
     * 区域
     */
    private String area;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 报警阈值
     */
    private String triggerThreshold;

    /**
     * 事件
     */
    private String event;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件分类主键
     */
    private Long eventTypeId;

    /**
     * 事件分类编号
     */
    private String eventTypeCode;

    /**
     * 事件分类名称
     */
    private String eventTypeName;

    /**
     * 触发时间
     */
    private String triggerTime;

    /**
     * 超时处理时间
     */
    private Integer timeoutProcessTimes;

    /**
     * 超时处理时间单位
     */
    private String timeoutProcessTimesUnit;

    /**
     * 快照信息
     */
    private String snapshot;
}
