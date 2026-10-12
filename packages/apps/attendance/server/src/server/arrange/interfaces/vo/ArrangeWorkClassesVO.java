package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/11/16
 * @description 排班班次
 */
@Data
public class ArrangeWorkClassesVO {

    /**
     * 排班ID
     */
    private Long workId;

    /**
     * 考勤组ID
     */
    private Long attendanceGroupId;

    /**
     * 班次开始时间
     */
    private String startWorkTime;

    /**
     * 班次结束时间
     */
    private String endWorkTime;

    /**
     * 真正上班时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startRealTime;

    /**
     * 真正下班时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endRealTime;

    /**
     * 休息开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime restStartRealTime;

    /**
     * 休息结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime restEndRealTime;

}