package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.TaskStatusEnum;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class InspectRecordAndTaskDTO extends BasicTenantEntity<Long> {
    private Long id;

    /**
     * 任务ID（临时巡检没有任务ID）
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 计划名
     */
    private String planName;

    /**
     * 目标对象类型
     */
    private String targetCategory;

    /**
     * 目标对象ID
     */
    private Long targetId;

    /**
     * 目标对象名
     */
    private String targetName;

    /**
     * 状态
     */
    private InspectRecordStatusEnum status;

    /**
     * 巡检时间
     */
    private LocalDateTime time;

    /**
     * 组织机构ID
     */
    private Long organizationId;

    /**
     * 组织机构名
     */
    private String organizationName;

    /**
     * 岗位ID
     */
    private Long positionId;

    /**
     * 岗位名
     */
    private String positionName;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户姓名
     */
    private String nickname;

    /**
     * 巡检结果
     */
    private InspectRecordResultEnum result;

    /**
     * 备注
     */
    private String remark;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 执行状态：执行中 已结束 已停用
     */
    private TaskStatusEnum tasStatus;
}
