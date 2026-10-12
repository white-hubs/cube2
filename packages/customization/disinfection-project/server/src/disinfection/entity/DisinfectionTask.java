package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotificationTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 消杀任务表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_task")
public class DisinfectionTask extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 所属计划ID(通过计划创建就有)
     */
    private Long planId;
    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 所属项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 任务类型ID
     */
    @TableField("task_type_id")
    private Long taskTypeId;

    /**
     * 任务类型名称
     */
    @TableField("task_type_name")
    private String taskTypeName;

    /**
     * 任务时限配置json（为空表示永不截至）
     */
    @TableField("task_time_limit_config")
    private String taskTimeLimitConfig;

    /**
     * 项目负责人ID
     */
    @TableField("project_leader_id")
    private Long projectLeaderId;

    /**
     * 消杀频次
     */
    @TableField("disinfection_frequency")
    private String disinfectionFrequency;

    /**
     * 通知类型
     */
    @TableField("notification_type")
    private DisinfectionNotificationTypeEnum notificationType;

    /**
     * 通知的小组ID
     */
    @TableField("notification_group_id")
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
     * 消杀区域ID
     */
    private Long taskAreaId;

    /**
     * 附件（保存多个的文件ID）
     */
    private String appendix;
    /**
     * 执行者ID
     */
    private Long executorId;
    /**
     * 执行者小组ID
     */
    private Long executorGroupId;

    /**
     * 任务的截止时间
     */
    private LocalDateTime deadline;
    /**
     * 任务进度
     */
    private BigDecimal process;

    /**
     * 任务状态
     */
    private DisinfectionTaskStatusEnum status;
}