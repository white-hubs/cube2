package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengjian
 * @date 2022/11/21 15:48
 * @description 占用假期余额
 */
@Getter
@AllArgsConstructor
public class OccupyHolidayBalanceCommand {
    private String businessKey;
    private Float number;
    private Long employeeId;
    private Holiday.HolidayType holidayType;
}
