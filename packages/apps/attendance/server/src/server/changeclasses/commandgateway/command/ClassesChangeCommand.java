package com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway.command;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 班次ID 对应arrange_work_classes表的ID
 */
@Data
public class ClassesChangeCommand {
    //申请人ID
    private Long applicantId;
    //申请人姓名
    private String applicantName;
    //申请人部门
    private Long applicantDeptId;
    //被换人ID
    private Long changerId;
    //被换人姓名
    private String changerName;//被换人姓名
    //要换班次ID
    private Long fromClassesId;
    //被换班次ID
    private Long toClassesId;
    //换班原因
    private String reason;
    //申请时间
    private LocalDateTime applyTime;
    //审批完成时间
    private LocalDateTime applyDoneTime;
    //考情组ID
    private Long attendanceGroupId;
    /**
     * 考勤类型(值班/排班)
     * AttendanceGroupTypeEnum
     */
    private String workType;
    /**
     * 要换日期
     */
    private LocalDateTime fromDate;
    /**
     * 被换日期
     */
    private LocalDateTime toDate;
}
