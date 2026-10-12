package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WeekEnum;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/30
 * @description 当前班次信息
 */
@Data
public class CurrentClassesVO {

    /**
     * 排班ID
     */
    private Long workId;

    /**
     * 员工ID
     */
    private Long staffId;

    /**
     * 打卡日期
     */
    private String clockDate;

    private Integer week;

    private String weekName;

    private Boolean isHoliday;

    /**
     * 考勤组ID
     */
    private Long attendanceGroupId;

    /**
     * 排班班次ID
     */
    private Long arrangeClassesId;

    /**
     * 排班班次名称
     */
    private String arrangeClassesName;

    /**
     * 上班时间前
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beforeStartRealTime;

    /**
     * 上班时间后
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime afterStartRealTime;

    /**
     * 班次开始时间
     */
    private String startWorkTime;

    /**
     * 班次结束时间
     */
    private String endWorkTime;

    /**
     * 下班时间前
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beforeEndRealTime;

    /**
     * 下班时间后
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime afterEndRealTime;

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
     * 班次标识
     */
    private String mark;

    /**
     * 上班打卡时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startWorkClockTime;

    /**
     * 上班打卡时间时分秒
     */
    private String startWorkClockTimeStr;

    /**
     * 下班打卡时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endWorkClockTime;

    /**
     * 下班打卡时间时分秒
     */
    private String endWorkClockTimeStr;

    /**
     * 上班打卡状态
     */
    private String startWorkClockStatus;

    /**
     * 下班打卡状态
     */
    private String endWorkClockStatus;

    private List<Way> wayList;

    /**
     * app端打卡跳转地方
     */
    private String appJumpWhere;

    private Integer tenant;

    @Data
    public static class Way {

        private Long id;

        private String way;

        /**
         * GPS考勤地址
         */
        private String address;

        /**
         * 经度
         */
        private String latitude;

        /**
         * 纬度
         */
        private String longitude;

        /**
         * 考勤范围(单位：米)
         */
        private Integer effectiveRange;

        private String wifi;
        private String mac;

        private String deviceName;
        private String deviceModel;
        private String deviceCode;
        private String deviceGenre;
    }

    public void setWeek(Integer week) {
        this.week = week;
        this.weekName = WeekEnum.getWeekName(week);
    }

    public void setStartWorkClockTime(LocalDateTime startWorkClockTime) {
        this.startWorkClockTime = startWorkClockTime;
        if (null != startWorkClockTime) {
            this.startWorkClockTimeStr = DateUtils.format(startWorkClockTime, "HH:mm:ss");
        }
    }

    public void setEndWorkClockTime(LocalDateTime endWorkClockTime) {
        this.endWorkClockTime = endWorkClockTime;
        if (null != endWorkClockTime) {
            this.endWorkClockTimeStr = DateUtils.format(endWorkClockTime, "HH:mm:ss");
        }
    }

}