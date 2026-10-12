package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskTimeLimitConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotificationTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskInfoVO {
    /**
     * Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 项目ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 计划id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long planId;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 任务名称
     */
    private String taskName;
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
     * 消杀区域ID
     */
    private Long taskAreaId;
    /**
     * 消杀区域ID
     */
    private String taskAreaName;
    /**
     * 消杀频次
     */
    private String disinfectionFrequency;

    /**
     * 任务时限配置json（为空表示永不截至）
     */
    @JsonProperty("taskTimeLimitConfig")
    private PlanTaskTimeLimitConfigDTO taskTimeLimitConfigDTO;
    @JsonProperty("taskTimeLimitConfigStr")
    private String taskTimeLimitConfig;

    /**
     * 项目负责人ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectLeaderId;
    /**
     * 项目负责人
     */
    private String projectLeaderName;
    /**
     * 执行者小组ID
     */
    private Long executorGroupId;
    /**
     * 执行小组
     */
    private String groupName;
    /**
     * 执行人id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long executorId;

    /**
     * 执行人
     */
    private String executorName;
    /**
     * 任务状态
     */
    private DisinfectionTaskStatusEnum status;

    /**
     * 任务的截止时间
     */
    private LocalDateTime deadline;
    /**
     * 任务进度
     */
    private BigDecimal process;

    /**
     * 通知类型
     */
    private DisinfectionNotificationTypeEnum notificationType;

    /**
     * 通知的小组ID
     */
    private Long notificationGroupId;
    /**
     * 通知的小组名称
     */
    private String notificationGroupName;

    /**
     * 通知的部门ID
     */
    private Long notificationDeptId;
    /**
     * 通知的部门名称
     */
    private String notificationDeptName;

    /**
     * 通知的岗位ID
     */
    private Long notificationPostId;
    /**
     * 岗位名称
     */
    private String notificationPostName;
    /**
     * 通知的角色ID
     */
    private Long notificationRoleId;
    /**
     * 角色名称
     */
    private String notificationRoleName;

    /**
     * 附件（保存多个的文件ID）
     */
    private String appendix;

    private List<FileVO> files;
    /**
     * 发布时间
     */
    private LocalDateTime createTime;
    /**
     * 发布人
     */
    private String createBy;
    private String createName;

    private String disinfectionScope;

}