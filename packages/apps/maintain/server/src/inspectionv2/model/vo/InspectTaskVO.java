package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InspectTaskVO {

    private Long id;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 计划名
     */
    private String planName;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 巡检类型ID
     */
    private Long typeId;

    /**
     * 巡检类型名
     */
    private String typeName;

    /**
     * 执行状态：执行中 已结束 已停用
     */
    private String status;

    /**
     * 发布人ID
     */
    private String publisherId;

    /**
     * 发布人名
     */
    private String publisherName;

    /**
     * json字符串
     */
    private String limitTimeJson;

    /**
     * 精确到分钟
     */
    private Integer limitTotalMinute;

    /**
     * 接收类型，1＝个人，2＝部门
     */
    private String receiveType;

    /**
     * 接收部门
     */
    private String receiveDeptIds;
    private String receiveDeptNames;

    /**
     * 接收岗位
     */
    private String receivePostIds;
    private String receivePostNames;

    /**
     * 接收岗位
     */
    private String receiveRoleIds;
    private String receiveRoleNames;

    /**
     * 接收对象ID
     */
    private String receiveIds;
    private String receiveNames;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 配置巡检目标对象数
     */
    private Integer targetNum;

    /**
     * 记录完成率
     */
    private double recordCompletionRate;

    /**
     * 任务耗时
     */
    private Long timeConsuming;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private List<InspectRecordVO> recordVOS;

    /**
     * 稽查任务关联的任务id
     */
    private Long checkTaskId;
    /**
     * 是否稽查任务
     */
    private Boolean isCheck;
    /**
     * 子任务关联的父任务id
     */
    private Long parentId;
    /**
     * 是否需要拆解
     */
    private Boolean isSplit;
    /**
     * 任务级别
     */
    private String level;
    /**
     * 是否为紧急任务
     */
    private Boolean isUrgentTask;
    /**
     * 生效开始时间
     */
    private LocalDateTime validBeginTime;

    /**
     * 生效结束时间
     */
    private LocalDateTime validEndTime;

    /**
     * 计划对象集合
     */
    List<List<InspectTargetInfoDTO>> typeTargets;

}
