package com.guhecloud.cube.srv.gen.maintain.server.upkeep.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author lrj
 * @Description 维保计划
 * @date 2022-4-29 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaintenancePlan extends BasicEnhancedTenantEntity<Long> {

    /**
     * id
     */
    private Long id;
    /**
     * 维保计划名称
     */
    private String maintenancePlanName;

    /**
     * 责任人id
     */
    private String responsibilityPersonId;
    /**
     * 责任名称
     */
    private String responsibilityPersonName;
    /**
     * 责任类型
     */
    private String responsibilityType;
    /**
     * 维保周期
     */
    private Integer maintenanceCycle;
    /**
     * 状态
     */
    private String status;
    /**
     * 计划生效时间
     */
    private LocalDateTime planStartTime;
    /**
     * 计划失效时间
     */
    private LocalDateTime planEndTime;

    /**
     * 维保周期单位
     */
    private String timeUnit;

    /**
     * 部门id
     */
    private String receiveDeptId;

    /**
     * 岗位id
     */
    private String receivePostId;

    /**
     * 发布时间点
     */
    private Integer maintenanceTimePoint;

    /**
     * 描述
     */
    private String remark;

    /**
     * 下次任务生成时间
     */
    private String nextTaskTime;
    /**
     * 生成任务方式
     */
    private String way;

}