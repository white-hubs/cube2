package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result;

import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttWorkingStaffResult {

    private Long staffId;
    private String staffName;
    private String mobile;
    private LocalDateTime startWorkTime;
    private LocalDateTime endWorkTime;
    private String className;
    private String classStartTime;
    private String classEndTime;
    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

    public String getClassStartTime() {
        if (startWorkTime == null) {
            return "";
        }
        return DateUtils.format(startWorkTime, "HH:mm");
    }

    public String getClassEndTime() {
        if (endWorkTime == null) {
            return "";
        }
        return DateUtils.format(endWorkTime, "HH:mm");
    }
}
