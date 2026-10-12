package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result;

import lombok.Data;

/**
 * 人员假期余额返回对象
 */
@Data
public class AttHolidayBalancePageDateResult {
    //员工id
    private Long staffId;
    /**
     * 假期类型
     */
    private Long holidayRuleId;
    /**
     * 可用余额
     */
    private String availableBalance;

}
