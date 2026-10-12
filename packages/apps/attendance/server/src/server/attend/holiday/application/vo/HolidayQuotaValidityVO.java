package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-29  14:14
 * @Description:
 * @Version: 1.0
 */
@Data
public class HolidayQuotaValidityVO {
    private Boolean longTerm;
    private Integer natureYear;
    private String validityNumber;
    private String validityUnit;

}
