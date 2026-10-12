package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param;

import lombok.Data;

@Data
public class AttDutyClassParam {
    private Long classId;
    private String className;
    private String classNickName;
    private String classType;
    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;
    /**
     * 上班时间
     */
    private String startWorkTime;

    /**
     * 下班时间
     */
    private String endWorkTime;
}
