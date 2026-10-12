package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.querygateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import lombok.Data;

@Data
public class LeaveTimeConvertQuery {
    private String leaveTimeStr;
    private Holiday.HolidayType holidayType;
}
