package com.guhecloud.cube.biz.linkage.server.linkage.vo;

import com.guhecloud.cube.biz.linkage.server.linkage.enumeration.EventTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventVO {

    private Long id;

    /**
     * 事件编号
     */
    private String code;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 规则主键
     */
    private Long ruleId;

    /**
     * 规则编号
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 触发时间
     */
    private LocalDateTime triggerTime;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 事件类型
     */
    private EventTypeEnum type;

    /**
     * 业务类型
     */
    private BusinessTypeEnum businessType;

    /**
     * 事件json
     */
    private String eventConfig;

    /**
     * 规则json
     */
    private String ruleConfig;

    /**
     * 触发级别
     */
    private Long triggerLevelId;

    /**
     * 触发类型
     */
    private Long triggerTypeId;

    /**
     * 操作人
     */
    private String operatorBy;

    /**
     * 操作时间
     */
    private String operatorName;

    /**
     * 触发事件的快照
     */
    private String snapshot;
}
