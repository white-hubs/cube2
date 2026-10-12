package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 线损报警规则
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineLossAlarmRule extends BasicTenantEntity<Long> {
    private Long id;
    /**
     * 触发的规则id
     */
    private Long triggeredRuleId;
    private String name;
    private String remark;
    /**
     * 报警级别
     */
    private Long alarmLevelId;
    /**
     * 报警级别名称
     */
    private String alarmLevelName;
    /**
     * 处理时限值
     */
    private Integer estimatedHandlerTime;
    /**
     * 处理时限名称
     */
    private String estimatedHandlerTimeName;
    /**
     * 处理时限单位
     */
    private String estimatedHandlerUnit;
    /**
     * 事件类型id
     */
    private Long emergencyEventTypeId;
    /**
     * 报警事件类型编码
     */
    private String emergencyEventTypeCode;
    /**
     * 事件类型名称
     */
    private String emergencyEventTypeName;
    /**
     * 触发条件,符合和值用逗号隔开，多组条件用分号隔开即：符合,值;符合,值(例如：EQ,10;GE,12)
     */
    private String condition;
    /**
     * 是否启用 (0-禁用，1-启用)
     */
    private Boolean enabled;
}
