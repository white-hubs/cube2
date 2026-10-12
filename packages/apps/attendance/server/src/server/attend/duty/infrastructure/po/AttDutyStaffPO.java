package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 值班人员
 */
@Data
@Builder
@TableName("att_duty_staff")
@EqualsAndHashCode(callSuper = true)
public class AttDutyStaffPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long staffId;
    private String staffName;
    private String dutyMonth;
}
