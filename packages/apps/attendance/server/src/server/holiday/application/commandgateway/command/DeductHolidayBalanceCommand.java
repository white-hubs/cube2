package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 扣减假期余额
 */
@Getter
@AllArgsConstructor
public class DeductHolidayBalanceCommand {
    /**
     * 员工id
     */
    private Long employeeId;

    private Float number;

    private String businessKey;

    private Holiday.HolidayType holidayType;
}
