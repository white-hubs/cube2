package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 假期余额分页返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttAskLeaveLedgerPageResult {

    private Long total;
    private Long pages;
    private List<AttAskLeaveLedgerPageData> records;
    // 表头
    List<AttHolidayRuleListResult> tableTitle;

    @Data
    public static class AttAskLeaveLedgerPageData {
        private Long id;
        private Long staffId;
        private String staffName;
        private Long deptId;
        private String deptName;
        private Long groupId;
        private String groupName;
        /**
         * 请假数据列表
         */
        private List<AttAskLeaveLedgerPageDateResult> ledgerValueList;
    }
}
