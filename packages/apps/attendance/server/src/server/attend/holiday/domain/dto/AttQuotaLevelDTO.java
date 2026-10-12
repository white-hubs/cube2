package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto;

import lombok.Data;

/**
 * 假期额度层级
 */
@Data
public class AttQuotaLevelDTO {

    private int ageYear;
    private int holidayNumber;
    private String unit;
}
