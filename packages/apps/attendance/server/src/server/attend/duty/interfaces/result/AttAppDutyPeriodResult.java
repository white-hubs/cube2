package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result;

import lombok.Data;

import java.util.List;

/**
 * app某月排班返回对象
 */
@Data
public class AttAppDutyPeriodResult {
    /**
     * 日期
     */
    private String day;
    /**
     * 日期下的排班
     */
    private List<AttDutyAppDayStaffResult> staffList;
}
