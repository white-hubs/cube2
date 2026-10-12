package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result;

import lombok.Data;

/**
 * 人员余额统计返回对象
 */
@Data
public class AttStaffTotalBalanceResult {
    private String total;
    private Boolean notLimit;
}
