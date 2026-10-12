package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HolidayTypeGrantConfigDTO {
    private Long holidayTypeId;
    private Holiday.HolidayType holidayType;
    private String holidayTypeCode;
    private String grantTime;
    private String grantQuota;
    private Integer tenant;
}
