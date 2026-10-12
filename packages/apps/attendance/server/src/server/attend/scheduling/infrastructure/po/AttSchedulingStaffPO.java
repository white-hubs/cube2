package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.filter.AttendanceCurrentUserFieldFilter;
import com.guhecloud.cube.srv.gen.attendance.management.server.filter.AttendanceCustomDepFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 排班人员
 */
@Data
@Builder
@TableName("att_scheduling_staff")
@EqualsAndHashCode(callSuper = true)
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "staff_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "staff_id", fieldFilter = AttendanceCurrentUserFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "staff_id", fieldParamConvert = AttendanceCustomDepFieldFilter.class)
}
)
public class AttSchedulingStaffPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    private Long staffId;
    private String staffName;
    private Long groupId;
    private String schedulingMonth;
}
