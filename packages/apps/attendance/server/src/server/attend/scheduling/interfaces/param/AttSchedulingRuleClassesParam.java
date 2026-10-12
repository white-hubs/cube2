package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 排班规则班次入参
 */
@Data
public class AttSchedulingRuleClassesParam {
    /**
     * 班次id
     */
    private Long id;
    /**
     * 规则id
     */
    private Long rulesId;
    /**
     * 星期(枚举)
     */
    private String week;

    /**
     * 上班时间
     */
    private String startWorkTime;

    /**
     * 上班打卡开始时间
     */
    private String startClockBeforeTime;

    /**
     * 上班打卡结束时间
     */
    private String startClockAfterTime;

    /**
     * 下班时间
     */
    private String endWorkTime;

    /**
     * 下班打卡开始时间
     */
    private String endClockBeforeTime;

    /**
     * 下班打卡结束时间
     */
    private String endClockAfterTime;

    /**
     * 休息开始时间
     */
    private String restStartTime;

    /**
     * 休息结束时间
     */
    private String restEndTime;

    /**
     * 班次类型
     * 枚举AttClassesTypeEnum，（如果有需求可拓展成班次类型模块）
     */
    private String classesType;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

    public LocalDateTime getDateStartClockBeforeTime(String ymd) {
        return DateUtils.parse(ymd + " " + this.getStartClockBeforeTime() + ":00");
    }

    public LocalDateTime getDateStartWorkTime(String ymd) {
        return DateUtils.parse(ymd + " " + this.getStartWorkTime() + ":00");
    }

    public LocalDateTime getDateStartClockAfterTime(String ymd) {
        return DateUtils.parse(ymd + " " + this.getStartClockAfterTime() + ":00");
    }

    public LocalDateTime getDateEndClockBeforeTime(String ymd) {
        return DateUtils.parse(ymd + " " + this.getEndClockBeforeTime() + ":00");
    }

    public LocalDateTime getDateEndWorkTime(String ymd) {
        return DateUtils.parse(ymd + " " + this.getEndWorkTime() + ":00");
    }

    public LocalDateTime getDateEndClockAfterTime(String ymd) {
        return DateUtils.parse(ymd + " " + this.getEndClockAfterTime() + ":00");
    }

    public LocalDateTime getDateRestStartTime(String ymd) {
        if (StringUtils.isEmpty(this.getRestStartTime())) {
            return null;
        }
        return DateUtils.parse(ymd + " " + this.getRestStartTime() + ":00");
    }

    public LocalDateTime getDateRestEndTime(String ymd) {
        if (StringUtils.isEmpty(this.getRestEndTime())) {
            return null;
        }
        return DateUtils.parse(ymd + " " + this.getRestEndTime() + ":00");
    }

}
