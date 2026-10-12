package com.guhecloud.cube.biz.daily.task.migration.po;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * <p>
 * 巡检计划
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@TableName("inspect_plan")
public class MigrationInspectPlanPO extends BasicTenantEntity<Long> {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private Long typeId;

    /**
     * 类型名
     */
    private String typeName;

    /**
     * 级别 紧急/普通 InspectLevelEnum
     */
    private String level;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 生效开始时间
     */
    private LocalDateTime validBeginTime;

    /**
     * 生效结束时间
     */
    private LocalDateTime validEndTime;

    /**
     * 1开启 0关闭
     */
    private Boolean enable = true;

    /**
     * 执行状态：执行中 已结束 已停用 InspectPlanExecStatusEnum
     */
    private String status;

    /**
     * json字符串
     */
    private String limitTimeJson;

    /**
     * 精确到分钟
     */
    private Integer limitTotalMinute;

    /**
     * 循环类型 0=单次 1=循环
     */
    private Boolean cycle;

    /**
     * 表达式ID
     */
    private Long cronId;

    /**
     * 表达式名
     */
    private String cronName;

    /**
     * cron表达式
     */
    private String cronExpression;

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
     * 是否需要拆解
     */
    private boolean isSplit;

    /**
     * 是否需要稽查
     */
    private Boolean isCheck;

    /**
     * 稽查人员类型1个人，2部门
     */
    private String checkUserType;

    /**
     * 稽查部门
     */
    private String checkDeptIds;

    /**
     * 稽查岗位
     */
    private String checkPostIds;

    /**
     * 稽查角色
     */
    private String checkRoleIds;

    /**
     * 稽查人ids
     */
    private String checkUserIds;

    /**
     * 配置巡检目标对象数
     */
    private Integer targetNum;

    /**
     * 执行次数
     */
    private Integer execTimes;

    /**
     * xxljob的任务ID
     */
    private Integer xxlTaskId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者ID
     */
    private Long creatorId;
    /**
     * 巡检方式
     */
    private String method;

    /**
     * 业务类型
     */
    private String businessType;
}
