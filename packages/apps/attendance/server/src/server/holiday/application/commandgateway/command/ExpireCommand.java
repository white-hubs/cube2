package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 假期失效命令
 */
@Getter
@AllArgsConstructor
public class ExpireCommand {
    private Holiday.HolidayType holidayType;
    private Long employeeId;
    private Float number;
}
