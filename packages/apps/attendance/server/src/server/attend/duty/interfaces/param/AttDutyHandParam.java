package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param;

import lombok.Data;

import java.util.List;

/**
 * 手动值班入参
 */
@Data
public class AttDutyHandParam {

    /**
     * 值班月份
     */
    private String yearMonth;
    /**
     * 值班人员集合
     */
    private List<AttDutyStaffParam> dutyStaffList;

}
