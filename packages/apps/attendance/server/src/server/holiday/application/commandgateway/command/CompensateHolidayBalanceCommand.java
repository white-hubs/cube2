package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 假期余额回滚补偿
 */
@Getter
@AllArgsConstructor
public class CompensateHolidayBalanceCommand {
    private Long employeeId;
    private String businessKey;
    private Float number;
    private Holiday.HolidayType holidayType;
}
