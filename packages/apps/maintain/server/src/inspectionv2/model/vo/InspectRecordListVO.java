package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InspectRecordListVO {

    private Long id;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 计划名
     */
    private String planName;

    /**
     * 任务ID（临时巡检没有任务ID）
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

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
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 设备区域id
     */
    private String areaId;
    /**
     * 设备区域名称
     */
    private String areaName;
}
