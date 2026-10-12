package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskTimeLimitConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskPageVO {
    /**
     * 主键ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 所属计划ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long planId;

    /**
     * 所属计划名称
     */
    private String planName;

    /**
     * 任务名称
     */
    private String taskName;

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
     * 任务类型ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long taskTypeId;

    /**
     * 任务类型名称
     */
    private String taskTypeName;

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
     * 项目负责人名称
     */
    private String projectLeaderName;

    /**
     * 消杀频次
     */
    private String disinfectionFrequency;

    /**
     * 通知类型
     */
    private String notificationType;

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

    /**
     * 消杀区域ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long taskAreaId;

    /**
     * 消杀区域名称
     */
    private String taskAreaName;

    /**
     * 附件（保存多个的文件ID）
     */
    private String appendix;

    /**
     * 任务截止时间
     */
    private LocalDateTime deadline;
    /**
     * 附件文件信息
     */
    private List<FileVO> files;
    /**
     * 执行者ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long executorId;

    private String executorName;
    /**
     * 执行者小组ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long executorGroupId;
    /**
     * 任务进度
     */
    private BigDecimal process;

    /**
     * 任务状态
     */
    private DisinfectionTaskStatusEnum status;
}
