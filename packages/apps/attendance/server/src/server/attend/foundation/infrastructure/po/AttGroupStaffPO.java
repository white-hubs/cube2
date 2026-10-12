package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.filter.AttendanceCurrentUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 考勤组人员
 */
@Data
@Builder
@TableName("att_group_staff")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "staff_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "dept_id", fieldFilter = AttendanceCurrentUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "dept_id")
}
)
public class AttGroupStaffPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 员工id
     */
    private Long id;
    /**
     * 员工id
     */
    private Long staffId;
    /**
     * 入职日期
     */
    private LocalDateTime entryDate;
    /**
     * 考勤组id
     */
    private Long groupId;
    /**
     * 考勤组名称
     */
    private String groupName;
    /**
     * 考勤组排班规则id
     */
    private Long schedulingRuleId;
    /**
     * 考勤组打卡规则id
     */
    private Long clockRuleId;
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
}
