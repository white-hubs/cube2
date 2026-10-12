package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param;

import lombok.Data;

import java.util.List;

@Data
public class AttDutyDateParam {
    /**
     * 日期
     */
    private String dutyDate;
    /**
     * 星期
     */
    private int week;
    /**
     * 日期下的班次集合
     */
    private List<AttDutyClassParam> dateClassList;

}
