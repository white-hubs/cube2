package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EmployeeBalanceDTO {
    @Getter
    private Long userId;
    @Getter
    private Holiday.HolidayType holidayType;

    private LocalDateTime timeOfInduction;

    private Long organizationId;

    private Long attendanceGroupId;
    @Getter
    private String employeeName;
    private String maternityLeave;
    private String compensatoryLeave;
    private String paternityLeave;
    private String bereavementLeave;
    private String compassionateLeave;
    private String marriageLeave;
    private String sickLeave;
    private String annualLeave;

}
