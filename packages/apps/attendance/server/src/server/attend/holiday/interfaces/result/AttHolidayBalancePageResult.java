package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 假期余额分页返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttHolidayBalancePageResult {

    private Long total;
    private Long pages;
    private List<AttHolidayBalancePageData> records;
    // 表头
    List<AttHolidayRuleListResult> tableTitle;

    @Data
    public static class AttHolidayBalancePageData {
        private Long id;
        private Long staffId;
        private String staffName;
        private Long deptId;
        private String deptName;
        private Long groupId;
        private String groupName;
        /**
         * 入职日期
         */
        private LocalDateTime enterDate;
        /**
         * 余额列表(key是假期类型的id，value是余额
         */
        private List<AttHolidayBalancePageDateResult> typeValueList;
    }
}
