package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceCollectionVO {
    private Long id;
    private String employeeName;
    private String attendanceGroupName;
    private String departmentName;
    private LocalDateTime timeOfInduction;
    /**
     * 年假天数
     */
    private Integer annualLeaveDay;

    /**
     * 调休小时
     */
    private Float compensatoryLeaveHour;

    /**
     * 事假
     */
    private Integer compassionateLeaveDay;

    /**
     * 病假
     */
    private Integer sickLeaveDay;

    /**
     * 产假
     */
    private Integer maternityLeaveDay;

    /**
     * 陪产假
     */
    private Integer paternityLeaveDay;

    /**
     * 丧假
     */
    private Integer bereavementLeaveDay;
}
