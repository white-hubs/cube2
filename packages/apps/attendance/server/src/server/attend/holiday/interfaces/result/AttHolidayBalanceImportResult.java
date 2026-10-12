package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result;

import lombok.Data;

import java.util.List;

/**
 * 假期余额导入结果
 */
@Data
public class AttHolidayBalanceImportResult {
    private Integer fail;
    private Integer success;
    List<String> errorMsg;
}
