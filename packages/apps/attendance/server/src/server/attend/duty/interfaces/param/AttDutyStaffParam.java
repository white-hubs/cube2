package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param;

import lombok.Data;

import java.util.List;

/**
 * 手动值班人员
 */
@Data
public class AttDutyStaffParam {
    private Long staffId;
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
     * 人员值班日期集合
     */
    private List<AttDutyDateParam> staffDateList;
}
