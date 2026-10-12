package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SafetyInspectRecord {
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
    private String status;

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
    private String result;

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

    private Integer tenant;
    private Boolean deleted;
    private String createBy;

    private String creatorId;

    private ActorTypeEnum creatorType;

    private LocalDateTime createTime;

    private String updateBy;

    private String updaterId;

    private ActorTypeEnum updaterType;

    private LocalDateTime updateTime;

    private Integer marketTenant;
    private String marketName;

    /**
     * 业务类型
     */
    private String businessType;
}
