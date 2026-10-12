package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 出差台账分页对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttAwayBusinessLedgerPageResult {
    private Long id;
    private Long staffId;
    private String staffName;
    private Long deptId;
    private String deptName;
    private Long groupId;
    private String groupName;
    /**
     * 出差次数
     */
    private Integer awayCount;
    /**
     * 出差时长
     */
    private Long awayNumber;
    /**
     * 关联出差单
     */
    private String awayFromIds;
    /**
     *
     */
    private String awayNumberShow;

    /**
     * 显示的出差时长
     *
     * @return 时长
     */
    public String getAwayNumberShow() {
        if (awayNumber != null && awayNumber > AttConstants.zeroInt) {
            return AttHolidayDomain.longToUnit(awayNumber, AttConstants.HOUR, AttConstants.dayOfHour, false);
        }
        return AttConstants.zeroString;
    }

}
