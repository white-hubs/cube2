package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("holiday_balance")
public class HolidayBalancePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long userId;
    private Holiday.HolidayType holidayType;
    /**
     * 剩余余额
     */
    @DiffLogField(name = "剩余余额")
    private Float remainBalance;
    /**
     * 占用余额
     */
    @DiffLogField(name = "占用余额")
    private Float occupyBalance;

    /**
     * (修改余额时的)备注
     */
    @Length(max = 200, message = "备注最大长度为200")
    private String remark;

}
