package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param;

import lombok.Data;

import java.util.List;

@Data
public class AttDutyRemoveStaffParam {
    private String yearMonth;
    private List<Long> staffIdList;
}
