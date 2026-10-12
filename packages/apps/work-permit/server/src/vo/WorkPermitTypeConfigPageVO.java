package com.guhecloud.cube.srv.gen.work.permit.server.vo;

import lombok.Data;

/**
 * 作业类型配置分页列表。
 */
@Data
public class WorkPermitTypeConfigPageVO {
    /**
     * 主键。
     */
    private Long id;
    /**
     * 作业类型名称。
     */
    private String typeName;
    /**
     * 作业等级。
     */
    private String workPermitLevel;
    /**
     * 等级定义。
     */
    private String levelDefinition;
    /**
     * 作业检查项数。
     */
    private Integer riskCheckCount;
    /**
     * 作业验收项数。
     */
    private Integer acceptanceCount;
    /**
     * 作业票有效时长，单位小时。
     */
    private Integer permitValidHours;
}
