package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 离岗台账入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttLeavePostLedgerPageParam extends Paging {
    private String staffName;
    private Long deptId;
    private Long groupId;
    private Period period;
}
