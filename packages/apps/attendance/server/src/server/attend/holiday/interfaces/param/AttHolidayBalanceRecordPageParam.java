package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 假期余额记录分页列表条件入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttHolidayBalanceRecordPageParam extends Paging {
    private Long id;
    private Long staffId;
    private Long holidayRuleId;
}
