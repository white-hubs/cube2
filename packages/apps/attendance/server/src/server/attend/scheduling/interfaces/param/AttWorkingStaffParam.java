package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import lombok.Data;

@Data
public class AttWorkingStaffParam {
    private Long groupId;
    private Long deptId;
    private Long staffId;
    private String staffName;
}
