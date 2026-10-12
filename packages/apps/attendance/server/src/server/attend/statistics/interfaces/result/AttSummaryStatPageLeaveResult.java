package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 汇总统计假期数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttSummaryStatPageLeaveResult {
    private Long staffId;
    private Long holidayRuleId;
    private String holidayRuleName;
    private String number;
    private String unit;
}
