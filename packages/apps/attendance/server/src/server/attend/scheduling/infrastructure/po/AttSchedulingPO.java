package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 排班数据
 */
@Data
@Builder
@TableName("att_scheduling")
@EqualsAndHashCode(callSuper = true)
public class AttSchedulingPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 排班月份
     */
    private String schedulingMonth;
    /**
     * 排班日期
     */
    private LocalDateTime schedulingDate;
    /**
     * 排班规则id
     */
    private Long schedulingRuleId;
    /**
     * 打卡规则id
     */
    private Long clockRuleId;
    /**
     * 排班方式
     */
    private String schedulingWays;
    /**
     * 排班班次id
     */
    private Long classesId;
    /**
     * 排班班次名称
     */
    private String classesName;
    /**
     * 排班班次别名
     */
    private String classesNickName;
    /**
     * 班次类型
     */
    private String classesType;
    /**
     * 上班范围开始时间
     */
    private LocalDateTime classesBeforeStartTime;
    /**
     * 上班开始时间
     */
    private LocalDateTime classesRealStartTime;
    /**
     * 上班范围结束时间
     */
    private LocalDateTime classesAfterStartTime;
    /**
     * 下班范围开始时间
     */
    private LocalDateTime classesBeforeEndTime;
    /**
     * 下班打卡时间
     */
    private LocalDateTime classesRealEndTime;
    /**
     * 下班范围结束时间
     */
    private LocalDateTime classesAfterEndTime;
    /**
     * 休息开始时间
     */
    private LocalDateTime classesRestStartTime;
    /**
     * 休息结束时间
     */
    private LocalDateTime classesRestEndTime;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

    /**
     * 员工id
     */
    private Long staffId;
    /**
     * 员工名称
     */
    private String staffName;
    /**
     * 员工部门id
     */
    private Long staffDeptId;
    /**
     * 员工部门名称
     */
    private String staffDeptName;
    /**
     * 员工考勤组id
     */
    private Long staffGroupId;
    /**
     * 员工考勤组名称
     */
    private String staffGroupName;
    /**
     * 员工考勤组类型
     */
    private String staffGroupRanges;
    /**
     * 手动排班
     */
    private Boolean handScheduling;
    /**
     * 已经开始
     */
    private Boolean started;
    /**
     * 班次版本
     */
    private Integer classesVersion;
    /**
     * 是否生成打卡任务
     */
    private Boolean clockRead;
}
