package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.aggregate;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import lombok.Data;

import java.util.List;

@Data
public class AttGroupAggregate {

    private Long id;
    /**
     * 考勤组id
     */
    private Long groupId;
    /**
     * 员工id
     */
    private Long staffId;
    /**
     * 电话
     */
    private String staffPhone;
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
     * 考勤组员工
     */
    private List<AttGroupStaffPO> groupStaffList;

}
