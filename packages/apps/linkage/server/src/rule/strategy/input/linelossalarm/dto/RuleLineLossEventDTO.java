package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.linelossalarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线损报警事件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleLineLossEventDTO {

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
     * 快照
     */
    private String snapshot;
}
