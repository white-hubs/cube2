package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskTimeLimitConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotificationTypeEnum;
import lombok.Data;

@Data
public class TaskUpsertParam {
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 所属计划ID
     */
    private Long planId;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 任务类型ID
     */
    private Long taskTypeId;

    /**
     * 任务类型名称
     */
    private String taskTypeName;

    /**
     * 任务时限配置json（为空表示永不截至）
     */
    private PlanTaskTimeLimitConfigDTO taskTimeLimitConfig;

    /**
     * 项目负责人ID
     */
    private Long projectLeaderId;

    /**
     * 消杀频次
     */
    private String disinfectionFrequency;

    /**
     * 通知类型
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
     * 消杀区域ID
     */
    private Long taskAreaId;

    /**
     * 附件（保存多个的文件ID）
     */
    private String appendix;
}
