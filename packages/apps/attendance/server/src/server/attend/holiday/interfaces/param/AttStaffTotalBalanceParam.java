package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param;

import lombok.Data;

@Data
public class AttStaffTotalBalanceParam {
    private Long staffId;
    private Long holidayRuleId;
    private String unit;
}
