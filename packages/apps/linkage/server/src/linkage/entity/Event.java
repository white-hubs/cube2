package com.guhecloud.cube.biz.linkage.server.linkage.entity;

import com.guhecloud.cube.biz.linkage.server.linkage.enumeration.EventTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Event extends BasicEnhancedTenantEntity<Long> {

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
    private String businessType;

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
     * 触发对象唯一标识
     */
    private String triggerTargetId;

    /**
     * 操作人
     */
    private String operatorBy;

    /**
     * 操作时间
     */
    private String operatorName;

    /**
     * 物联网事件实例主键
     */
    private String iotEventInstanceId;

    /**
     * 业务快照的信息
     */
    private String snapshot;
}
