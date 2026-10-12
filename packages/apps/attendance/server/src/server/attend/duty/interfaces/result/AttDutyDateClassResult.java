package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 排班人日期班次
 */
@Data
public class AttDutyDateClassResult {
    private String yearMonth;
    private LocalDateTime dutyDate;
    private Long classesId;
    private String classesName;
    private String classesNickName;
    private String classesType;
    private LocalDateTime classesRealStartTime;
    private LocalDateTime classesRealEndTime;
    /**
     * 跨天班次
     */
    private Boolean classesEndTimeIsMorrow;
}
