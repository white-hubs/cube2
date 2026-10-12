package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskCirculateConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskTimeLimitConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionEnableEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotificationTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionPlanStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlanDetailVO {
    /**
     * 主键ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 所属项目ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 计划生效时间
     */
    private LocalDateTime beginTime;

    /**
     * 计划失效时间（为空表示永不失效）
     */
    private LocalDateTime endTime;

    /**
     * 任务生成周期配置（json）
     */
    private List<PlanTaskCirculateConfigDTO> taskGenerationCycleConfigs;

    /**
     * 项目负责人ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectLeaderId;
    /**
     * 项目负责人名称
     */
    private String projectLeaderName;

    /**
     * 任务时限配置（为空表示永不截至）
     */
    private PlanTaskTimeLimitConfigDTO taskTimeLimitConfig;

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

    private boolean enableBool;

    /**
     * 计划任务配置列表
     */
    private List<PlanTaskConfig> planTaskConfigList;

    private LocalDateTime createTime;
    private String createBy;
    private String createName;

    @Data
    public static class PlanTaskConfig {

        /**
         * 主键ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long id;
        /**
         * 项目ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long projectId;

        private String projectName;
        /**
         * 计划ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long planId;

        private String planName;

        /**
         * 任务类型ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long taskTypeId;

        /**
         * 任务类型名称
         */
        private String taskTypeName;
        /**
         * 消杀任务名称
         */
        private String taskName;

        /**
         * 消杀区域ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long taskAreaId;

        private String taskAreaName;
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
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long notificationGroupId;
        /**
         * 提醒小组名称
         */
        private String notificationGroupName;

        /**
         * 通知的部门ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long notificationDeptId;

        private String notificationDeptName;
        /**
         * 通知的岗位ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long notificationPostId;
        private String notificationPostName;
        /**
         * 通知的角色ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long notificationRoleId;
        private String notificationRoleName;
    }

}
