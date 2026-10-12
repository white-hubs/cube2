package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result;

import lombok.Data;

import java.util.List;

@Data
public class AttDutyPageResult {

    private Long staffId;
    private String staffName;
    private Long staffDeptId;
    private String staffDeptName;
    List<AttDutyDateClassResult> dateClassList;
}
