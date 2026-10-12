package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result;

import lombok.Data;

/**
 * 假期规则下拉列表（假期类型
 */
@Data
public class AttHolidayRuleListResult {
    private Long id;
    private String name;
    /**
     * 不限额
     */
    private Boolean notLimit;
    private String unit;
}
