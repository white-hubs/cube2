package com.guhecloud.cube.biz.daily.task.migration.po;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 巡检任务实体
 */
@Data
//@TableName("inspect_task")
@EqualsAndHashCode(callSuper = true)
public class MigrationInspectTaskPO extends BasicTenantEntity<Long> {

    private Long id;

    /**
     * 任务编码
     */
    private String code;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 父类计划Id
     */
    private Long parentId;

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
     * 级别 紧急/普通 InspectLevelEnum
     */
    private String level;

    /**
     * 是否需要拆解
     */
    private Boolean isSplit = false;

    /**
     * 稽查任务id(父任务id)
     */
    private Long checkTaskId;

    /**
     * 执行状态：执行中 已结束 已停用 InspectTaskStatusEnum
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

    /**
     * 接收岗位
     */
    private String receivePostIds;
    /**
     * 接收角色
     */
    private String receiveRoleIds;
    /**
     * 接收对象ID
     */
    private String receiveIds;

    /**
     * 协同人
     */
    private String assistIds;

    /**
     * 生效开始时间
     */
    private LocalDateTime validBeginTime;

    /**
     * 生效结束时间
     */
    private LocalDateTime validEndTime;

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
     * 对象ids
     */
    private String targetIds;

    /**
     * 备注
     */
    private String remark;
    /**
     * 巡检方式
     */
    private String method;

    /**
     * 巡检结果
     */
    private String result;

    /**
     * 业务类型
     */
    private String businessType;
}
