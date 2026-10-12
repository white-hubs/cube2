package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HolidayTypeVO {
    private String label;
    private String value;
    private Long id;
    // 请假规则
    private LeaveRule leaveRule;

    @Builder
    @Getter
    public static class LeaveRule {
        /**
         * 最小请假时长
         */
        private com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule.MinimumLeaveTimeUnit minimumLeaveTimeUnit;
    }
}
