package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.vo;

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
public class EmployeeBalanceVO {
    @Getter
    private Long userId;
    @Getter
    private Holiday.HolidayType holidayType;

    private LocalDateTime timeOfInduction;

    private Long organizationId;

    private Long attendanceGroupId;
    @Getter
    private String employeeName;
    private String maternityLeaveDay;
    private String compensatoryLeaveHour;
    private String paternityLeaveDay;
    private String bereavementLeaveDay;
    private String compassionateLeaveDay;
    private String marriageLeaveDay;
    private String sickLeaveDay;
    private String annualLeaveDay;

    public String getMarriageLeaveDay() {
        return handle(marriageLeaveDay);
    }

    public String getPaternityLeaveDay() {
        return handle(paternityLeaveDay);
    }

    public String getBereavementLeaveDay() {
        return handle(bereavementLeaveDay);
    }

    public String getCompassionateLeaveDay() {
        return handle(compassionateLeaveDay);
    }

    public String getMaternityLeaveDay() {
        return handle(maternityLeaveDay);
    }

    public String getSickLeaveDay() {
        return handle(sickLeaveDay);
    }

    public String getAnnualLeaveDay() {
        return handle(annualLeaveDay);
    }

    /**
     * 保留一位小数
     *
     * @param balance
     */
    public String handle(String balance) {
        if (balance.contains(".")) {
            return balance.substring(0, balance.indexOf(".") + 2);
        }
        return balance;
    }

    public String getCompensatoryLeaveHour() {
        return handle(compensatoryLeaveHour);
    }
}
