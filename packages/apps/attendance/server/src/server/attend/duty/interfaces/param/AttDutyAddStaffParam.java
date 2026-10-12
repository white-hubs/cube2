package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param;

import lombok.Data;

import java.util.List;

@Data
public class AttDutyAddStaffParam {
    private String yearMonth;
    private List<AttDutyStaffParam> staffList;
}
