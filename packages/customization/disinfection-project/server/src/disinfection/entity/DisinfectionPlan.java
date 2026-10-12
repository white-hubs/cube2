package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionEnableEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionPlanStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 消杀计划实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_plan")
public class DisinfectionPlan extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 计划名称
     */
    @TableField("plan_name")
    private String planName;

    /**
     * 所属项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 计划生效时间
     */
    @TableField("begin_time")
    private LocalDateTime beginTime;

    /**
     * 计划失效时间（为空表示永不失效）
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 任务生成周期配置（json）
     */
    @TableField("task_generation_cycle_config")
    private String taskGenerationCycleConfig;

    /**
     * 项目负责人ID
     */
    @TableField("project_leader_id")
    private Long projectLeaderId;

    /**
     * 任务时限配置（为空表示永不截至）
     */
    @TableField("task_time_limit_config")
    private String taskTimeLimitConfig;

    /**
     * 备注
     */
    private String remark;

    /**
     * 计划状态
     */
    private DisinfectionPlanStatusEnum status;

    /**
     * 是否启用
     */
    private DisinfectionEnableEnum enable;
}