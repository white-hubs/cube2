package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("change_holiday_balance_log")
public class ChangeHolidayBalanceLogPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long userId;
    private Long staffId;
    private Float originalBalance;
    private Float expectedBalance;

    private Holiday.HolidayType holidayType;
}
