package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 排班班次对象
 */
@Data
public class AttSchedulingClasses {
    private Long id;
    /**
     * 排班日期
     */
    private LocalDateTime schedulingDate;

    /**
     * 排班班次
     */
    private Long classesId;
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
     * 星期
     */
    private Integer week;
    /**
     * 班次类型
     * 枚举AttClassesTypeEnum，（如果有需求可拓展成班次类型模块）
     */
    private String classesType;
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
     * 手动排班
     */
    private Boolean handScheduling;
    /**
     * 已经开始
     */
    private Boolean started;
}
