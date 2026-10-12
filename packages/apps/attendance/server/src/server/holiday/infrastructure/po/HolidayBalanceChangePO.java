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
@TableName("holiday_balance_change")
public class HolidayBalanceChangePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Float value;
    private String remark;
    private Long userId;
    private Holiday.HolidayType holidayType;
    /*
     * 实扣 预扣 发放
     */
    private String changeType;

}
