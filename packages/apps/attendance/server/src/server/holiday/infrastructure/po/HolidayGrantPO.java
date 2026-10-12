package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("holiday_grant")
@AllArgsConstructor
@NoArgsConstructor
public class HolidayGrantPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Float balance;
    private String remark;
    private Holiday.HolidayType holidayType;
    private Long userId;
    private Float remainingBalance;
}
