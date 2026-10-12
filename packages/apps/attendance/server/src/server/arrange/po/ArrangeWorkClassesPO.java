package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/11/16
 * @description 排班班次
 */
@Data
@TableName("arrange_work_classes")
public class ArrangeWorkClassesPO extends Classes {

    //    private Long id;

    /**
     * 真实上班时间，如：2022-09-01 09:00
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startRealTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endRealTime;

    /**
     * 上班时间前
     */
    private LocalDateTime beforeStartRealTime;

    /**
     * 上班时间后
     */
    private LocalDateTime afterStartRealTime;

    /**
     * 下班时间前
     */
    private LocalDateTime beforeEndRealTime;

    /**
     * 下班时间后
     */
    private LocalDateTime afterEndRealTime;

    /**
     * 休息开始时间
     */
    private LocalDateTime restStartRealTime;

    /**
     * 休息结束时间
     */
    private LocalDateTime restEndRealTime;

    // 将模板时间格式化成具体的时间，如：上班08:00 -》 实际上班2022-12-19 08:00:00
    public void findRealTime(String date) {
        // 非休息班次的班次计算出实际上下班时间
        if (!ClassesMarkEnum.All_DAY_REST.name().equals(this.getMark()) && StringUtils.isNotEmpty(date)) {
            this.startRealTime = DateUtils.parse(date + " " + this.getStartWorkTime() + ":59", "yyyy-MM-dd HH:mm:ss");
            this.endRealTime = DateUtils.parse(date + " " + this.getEndWorkTime() + ":00", "yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotEmpty(this.getBeforeStartWorkTime())) {
                this.beforeStartRealTime = DateUtils.parse(date + " " + this.getBeforeStartWorkTime() + ":00", "yyyy-MM-dd HH:mm:ss");
                if (this.beforeStartRealTime.isAfter(this.startRealTime)) {
                    this.beforeStartRealTime = DateUtils.offsetDay(this.beforeStartRealTime, -1);
                }
            }
            if (StringUtils.isNotEmpty(this.getAfterStartWorkTime())) {
                this.afterStartRealTime = DateUtils.parse(date + " " + this.getAfterStartWorkTime() + ":59", "yyyy-MM-dd HH:mm:ss");
                if (this.getEndTimeIsMorrow() && afterStartRealTime.isBefore(this.startRealTime)) {
                    this.afterStartRealTime = DateUtils.offsetDay(this.afterStartRealTime, 1);
                }
            }
            if (StringUtils.isNotEmpty(this.getBeforeEndWorkTime())) {
                this.beforeEndRealTime = DateUtils.parse(date + " " + this.getBeforeEndWorkTime() + ":00", "yyyy-MM-dd HH:mm:ss");
                if (this.getEndTimeIsMorrow() && beforeEndRealTime.isBefore(this.endRealTime)) {
                    this.beforeEndRealTime = DateUtils.offsetDay(this.beforeEndRealTime, 1);
                }
            }
            if (StringUtils.isNotEmpty(this.getAfterEndWorkTime())) {
                this.afterEndRealTime = DateUtils.parse(date + " " + this.getAfterEndWorkTime() + ":59", "yyyy-MM-dd HH:mm:ss");
                if (this.getEndTimeIsMorrow() || this.afterEndRealTime.isBefore(this.endRealTime)) {
                    this.afterEndRealTime = DateUtils.offsetDay(this.afterEndRealTime, 1);
                }
            }
            if (StringUtils.isNotEmpty(this.getRestStartTime())) {
                this.restStartRealTime = DateUtils.parse(date + " " + this.getRestStartTime() + ":00", "yyyy-MM-dd HH:mm:ss");
                if (this.getEndTimeIsMorrow() && this.restStartRealTime.isBefore(this.startRealTime)) {
                    this.restStartRealTime = DateUtils.offsetDay(this.restStartRealTime, 1);
                }
            }
            if (StringUtils.isNotEmpty(this.getRestEndTime())) {
                this.restEndRealTime = DateUtils.parse(date + " " + this.getRestEndTime() + ":00", "yyyy-MM-dd HH:mm:ss");
                if (this.getEndTimeIsMorrow() && this.restEndRealTime.isBefore(this.startRealTime)) {
                    this.restEndRealTime = DateUtils.offsetDay(this.restEndRealTime, 1);
                }
            }
            if (this.getEndTimeIsMorrow()) {
                /*//TODO 允许打下班卡的开始范围不一定就在第二天，可能还在前一天
                // 如果下班范围开始时间比下班时间小，那么就是第二天，如果比下班时间晚那么就是前一天
                if (this.beforeEndRealTime != null && DateUtils.compare(this.beforeEndRealTime, this.endRealTime) <= 0) {
                    this.beforeEndRealTime = DateUtils.offsetDay(this.beforeEndRealTime, 1);
                }*/
                this.endRealTime = DateUtils.offsetDay(this.endRealTime, 1);
                //this.afterEndRealTime = this.afterEndRealTime == null ? null : DateUtils.offsetDay(this.afterEndRealTime, 1);
            }
        }
    }

}