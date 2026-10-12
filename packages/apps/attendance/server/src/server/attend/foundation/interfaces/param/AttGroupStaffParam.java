package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class AttGroupStaffParam {
    /**
     * 员工id
     */
    @NotNull(message = "员工id不能为空")
    private Long id;
    /**
     * 员工id
     */
    private Long staffId;
    /**
     * 考勤组id
     */
    private Long groupId;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 员工名称
     */
    private String name;
    /**
     * 最后一级员工部门id
     */
    private Long deptId;
    /**
     * 最后一级员工部门名称
     */
    private String deptName;
    /**
     * 多级部门id
     */
    private String organizations;

    // 前端员工id放在id中的
    public Long getStaffId() {
        return this.id;
    }

}
