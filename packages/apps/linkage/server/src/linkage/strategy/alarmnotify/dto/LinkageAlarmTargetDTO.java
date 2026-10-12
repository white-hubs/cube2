package com.guhecloud.cube.biz.linkage.server.linkage.strategy.alarmnotify.dto;

import lombok.Data;

/**
 * 报警对象信息
 *
 * @author lanchun
 * @date 2022/6/25 17:01
 */
@Data
public class LinkageAlarmTargetDTO {

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
