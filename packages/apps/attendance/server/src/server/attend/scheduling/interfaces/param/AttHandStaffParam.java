package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import lombok.Data;

import java.util.List;

@Data
public class AttHandStaffParam {

    // 人员id
    private Long staffId;
    private String staffName;
    private Long groupId;
    private Long ruleId;
    // 日期集合
    private List<AttHandStaffDateParam> hsDateList;
}
