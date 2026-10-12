package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result;

import lombok.Data;

@Data
public class AttGroupStaffResult {
    private Long id;
    /**
     * 员工id
     */
    private Long staffId;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 员工名称
     */
    private String name;
    /**
     * 员工部门id
     */
    private Long deptId;
    /**
     * 员工部门名称
     */
    private String deptName;
    /**
     * 部门id(多级部门id)
     */
    private String organizations;

    public Long getId() {
        return this.getStaffId();
    }
}
