package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 设备条件事件
 *
 * @author lanchun
 * @date 2022/6/28 9:56
 */
@Data
@Builder
public class RuleDeviceConditionEventDTO {

    /**
     * 级别主键
     */
    private Long triggerLevelId;

    /**
     * 类型主键
     */
    private Long triggerTypeId;

    /**
     * 触发对象key
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
     * 区域
     */
    private String area;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 阈值
     */
    private String triggerThreshold;

    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 取得的属性的时间戳
     */
    private String propValueTs;
}
