package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttDutyAppDayStaffResult {
    private Long staffId;
    private String staffName;
    private String className;
    /**
     * 是否本人
     */
    private boolean isCurrentUser;
}
