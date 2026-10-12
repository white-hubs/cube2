package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 离岗台账分页对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttLeavePostLedgerPageResult {
    private Long id;
    private Long staffId;
    private String staffName;
    private Long deptId;
    private String deptName;
    private Long groupId;
    private String groupName;
    /**
     * 离岗次数
     */
    private Integer postCount;
    /**
     * 离岗时长
     */
    private Long postNumber;
    /**
     * 关联离岗单
     */
    private String postFromIds;
    /**
     *
     */
    private String postNumberShow;

    /**
     * 显示的离岗时长
     *
     * @return 时长
     */
    public String getPostNumberShow() {
        if (postNumber != null && postNumber > AttConstants.zeroInt) {
            return AttHolidayDomain.longToUnit(postNumber, AttConstants.HOUR, AttConstants.dayOfHour, false);
        }
        return AttConstants.zeroString;
    }

}
