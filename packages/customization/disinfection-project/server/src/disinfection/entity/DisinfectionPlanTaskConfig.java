package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotificationTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消杀任务配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_plan_task_config")
public class DisinfectionPlanTaskConfig extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;
    /**
     * 计划ID
     */
    @TableField("plan_id")
    private Long planId;

    /**
     * 任务类型ID
     */
    @TableField("task_type_id")
    private Long taskTypeId;

    private String taskTypeName;
    /**
     * 消杀任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 消杀区域ID
     */
    @TableField("task_area_id")
    private Long taskAreaId;

    /**
     * 消杀频次
     */
    @TableField("disinfection_frequency")
    private String disinfectionFrequency;

    /**
     * 通知方式
     */
    private DisinfectionNotificationTypeEnum notificationType;
    /**
     * 通知的小组ID
     */
    private Long notificationGroupId;

    /**
     * 通知的部门ID
     */
    private Long notificationDeptId;

    /**
     * 通知的岗位ID
     */
    private Long notificationPostId;

    /**
     * 通知的角色ID
     */
    private Long notificationRoleId;
    /**
     * 这个配置生成次数
     */
    private Integer generateCount;
}