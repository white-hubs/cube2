package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.filter.AttendanceCurrentUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  13:36
 * @Description: 打卡台账
 * @Version: 1.0
 */
@Data
@TableName("att_clock_in_ledger")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "org_id", fieldFilter = AttendanceCurrentUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "org_id")
}
)
public class AttClockInLedgerPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    private String userName;
    /**
     * 统计时间
     */
    private String ledgerDate;
    private Long orgId;
}
