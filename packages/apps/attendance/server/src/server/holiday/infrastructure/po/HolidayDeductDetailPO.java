package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 假期扣减明细
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("holiday_deduct_detail")
public class HolidayDeductDetailPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long userId;
    private Float value;
    private Holiday.HolidayType holidayType;
}
