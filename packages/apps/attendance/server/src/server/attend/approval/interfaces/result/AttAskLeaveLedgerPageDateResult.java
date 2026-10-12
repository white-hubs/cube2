package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import lombok.Data;

/**
 * 请假台账分页列表台账详情返回对象
 */
@Data
public class AttAskLeaveLedgerPageDateResult {
    /**
     * 员工id
     */
    private Long staffId;
    /**
     * 假期类型
     */
    private Long holidayRuleId;
    /**
     * 一条折算多少小时
     */
    private Integer dayOfHour;
    /**
     * 请假时长(毫秒)
     */
    private Long leaveNumber;
    /**
     * 显示的数量
     */
    private String leaveNumberShow;
    /**
     * 请假单ids
     */
    private String leaveFromIds;

    public String getLeaveNumberString() {
        if (leaveNumber == null || leaveNumber <= AttConstants.zeroLong) {
            return "";
        }
        return AttHolidayDomain.longToUnit(leaveNumber, AttConstants.DAYS, dayOfHour, false);
    }

}
