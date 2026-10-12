package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 线损报警规则分页列表 VO
 */
@Data
public class LineLossAlarmRulePageVO {

    private Long id;

    /**
     * 预警规则名称
     */
    private String name;

    /**
     * 报警级别 ID
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
     * 说明/描述
     */
    private String remark;

    /**
     * 是否启用 (0-禁用，1-启用)
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;
}
