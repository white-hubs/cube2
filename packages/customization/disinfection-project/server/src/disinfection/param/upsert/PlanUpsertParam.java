package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskCirculateConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskTimeLimitConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionEnableEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotificationTypeEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlanUpsertParam {

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 计划生效时间
     */
    private LocalDateTime beginTime;

    /**
     * 计划失效时间（为空表示永不失效）
     */
    private LocalDateTime endTime;

    /**
     * 任务生成周期配置
     */
    @NotNull(message = "任务生成周期配置不能为空")
    private List<PlanTaskCirculateConfigDTO> taskGenerationCycleConfigs;

    /**
     * 项目负责人ID
     */
    private Long projectLeaderId;

    /**
     * 任务时限配置（为空表示永不截至）
     */
    private PlanTaskTimeLimitConfigDTO taskTimeLimitConfig;

    /**
     * 备注
     */
    private String remark;

    /**
     * 启用状态
     */
    private DisinfectionEnableEnum enable;
    /**
     * 计划配置的任务信息
     */
    private List<ConfigTaskUpsertParam> planTaskConfigList;

    @Data
    public static class ConfigTaskUpsertParam {
        /**
         * 主键ID
         */
        private Long id;

        /**
         * 任务类型ID
         */
        private Long taskTypeId;
        /**
         * 消杀任务名称
         */
        private String taskTypeName;
        /**
         * 消杀任务名称
         */
        private String taskName;

        /**
         * 消杀区域ID
         */
        private Long taskAreaId;

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
         * 通知的部门ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long notificationDeptId;

        /**
         * 通知的岗位ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long notificationPostId;

        /**
         * 通知的角色ID
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long notificationRoleId;
    }
}
