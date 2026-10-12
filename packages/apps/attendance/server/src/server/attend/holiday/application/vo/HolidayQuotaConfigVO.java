package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-29  14:14
 * @Description:
 * @Version: 1.0
 */
@Data
public class HolidayQuotaConfigVO {
    private String quotaType;
    private Integer fixedNumber;
    private String fixedUnit;
    private List<HolidayQuotaConfigDetailsVO> quotaLevelList;

}
