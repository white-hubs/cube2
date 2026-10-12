package com.guhecloud.cube.srv.gen.attendance.management.server.stat.vo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
@TableName("staff_attendance_stat")
public class StaffAttendanceStatPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String month;
    private Long staffId;
    private String staffName;
    private Long staffOrganizationId;
    private Long groupId;
    private String statContent;
}
