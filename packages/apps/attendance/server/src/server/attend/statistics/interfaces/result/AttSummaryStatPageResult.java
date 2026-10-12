package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;
import lombok.Data;

import java.util.List;

/**
 * 汇总分页列表返回对象
 */
@Data
public class AttSummaryStatPageResult {
    private Long total;
    private Long pages;
    private List<AttSummaryStatPageDataResult> records;
    // 表头
    List<AttHolidayRuleListResult> tableTitle;
}
