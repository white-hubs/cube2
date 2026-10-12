package com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.domainservice;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.ArrangeWorkQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.LeaveTimeConvert;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 请假领域服务
 */
public class LeaveFormDomainService {

    /**
     * 请假时长计算
     */
    public static Float computeLeaveTime(Long employeeId, LeaveFormDetail.LeaveTime leaveTime,
                                         LeaveRule leaveRule
                                        ) {
        float hour = 0.0f;

        // 解析新时间格式
        /*
         * 如果是半天请假，计算多少个AM,PM
         * 思路：
         * Stack
         * String start = "AM";
         * int amCount = 0;
         * int pmCount = 0;
         * String end = "PM";
         * for (int i = 1; i <= day; i++) {
         * // AM,PM  AM,AM PM,PM PM,AM
         *
         *  if (start.equals("AM") && end.equals("PM")) {
         *      amCount++;
         *      pmCount++;
         *  }
         *
         *  if (start.equals("AM") && end.equals("AM")) {
         *       amCount++;
         *       if (如果不是最后一天) {
         *           pmCount++;
         *       }
         *  }
         *
         *  if (start.equals("PM") && end.equals("PM")) {
         *      pmCount++;
         *       if (如果不是第一天) {
         *       }
         *  }
         *
         *  if (start.equals("PM") && end.equals("AM")) {
         *      if (如果不是第一天) {
         *          amCount++;
         *      }
         *      if (如果不是最后一天) {
         *           pmCount++;
         *      }
         *  }
         * }
         */
        if (leaveTime.getStartTime().getTimeUnit() == LeaveFormDetail.LeaveTimeUnit.PM ||
            leaveTime.getStartTime().getTimeUnit() == LeaveFormDetail.LeaveTimeUnit.AM) {

            long day =
                    DateUtils.between(DateUtils.parseDate(leaveTime.getStartTime().getTime()), DateUtils.parseDate(leaveTime.getEndTime()
                                                                                                                           .getTime()), ChronoUnit.DAYS, true) +
                    1;
            LeaveFormDetail.LeaveTimeUnit startTimeUnit = leaveTime.getStartTime().getTimeUnit();
            LeaveFormDetail.LeaveTimeUnit endTimeUnit = leaveTime.getEndTime().getTimeUnit();
            int pmCount = 0;
            int amCount = 0;
            for (int i = 1; i <= day; i++) {
                if (startTimeUnit == LeaveFormDetail.LeaveTimeUnit.AM && endTimeUnit == LeaveFormDetail.LeaveTimeUnit.PM) {
                    amCount++;
                    pmCount++;
                }
                if (startTimeUnit == LeaveFormDetail.LeaveTimeUnit.AM && endTimeUnit == LeaveFormDetail.LeaveTimeUnit.AM) {
                    amCount++;
                    if (i != day) {
                        pmCount++;
                    }
                }
                if (startTimeUnit == LeaveFormDetail.LeaveTimeUnit.PM && endTimeUnit == LeaveFormDetail.LeaveTimeUnit.PM) {
                    pmCount++;
                    if (i != 1) {
                        amCount++;
                    }
                }
                if (startTimeUnit == LeaveFormDetail.LeaveTimeUnit.PM && endTimeUnit == LeaveFormDetail.LeaveTimeUnit.AM) {
                    if (i != 1) {
                        amCount++;
                    }
                    if (i != day) {
                        pmCount++;
                    }
                }

            }

            LeaveRule.LeaveTimeUnitConvert leaveTimeUnitConvert = leaveRule.getMinimumLeaveTimeUnitConvert().getLeaveTimeUnitConverts()
                    .stream().filter(convert -> convert instanceof LeaveRule.HalfDayConvert).findFirst()
                    .orElse(new LeaveRule.LeaveTimeUnitConvert());

            LeaveRule.HalfDayConvert halfDayConvert = (LeaveRule.HalfDayConvert) leaveTimeUnitConvert;
            Float pmHour = halfDayConvert.getPmHour();
            Float amHour = halfDayConvert.getAmHour();

            ArrangeWorkQueryService arrangeWorkQueryService = ApplicationContextHolder.getBean(ArrangeWorkQueryService.class);

            hour = pmHour * pmCount + amHour * amCount;

            float besideAm = 0.0F;
            float besidePm = 0.0F;
            if (leaveRule.getLeaveTimeConvert().getConvertWay() == LeaveTimeConvert.ConvertWay.WORKING_DAY) {
                LocalDateTime end = leaveTime.getPeriod().getEnd();
                for (LocalDateTime beginLocalDateTime = DateUtils.newLocalDateTime(leaveTime.getPeriod().getBegin());
                     beginLocalDateTime.equals(end) || beginLocalDateTime.isBefore(end); ) {
                    boolean isAM = beginLocalDateTime.isBefore(
                            LocalDateTime.of(beginLocalDateTime.getYear(), beginLocalDateTime.getMonth(), beginLocalDateTime.getDayOfMonth(), 12, 0)
                                                              ) && (beginLocalDateTime.isAfter(
                            LocalDateTime.of(beginLocalDateTime.getYear(), beginLocalDateTime.getMonth(), beginLocalDateTime.getDayOfMonth(), 0, 0, 0)
                                                                                              ) ||
                                                                    beginLocalDateTime.isEqual(LocalDateTime.of(beginLocalDateTime.getYear(), beginLocalDateTime.getMonth(), beginLocalDateTime.getDayOfMonth(), 0, 0)));

                    boolean isPM = beginLocalDateTime.isBefore(
                            LocalDateTime.of(beginLocalDateTime.getYear(), beginLocalDateTime.getMonth(), beginLocalDateTime.getDayOfMonth(), 23, 59)
                                                              ) && beginLocalDateTime.isAfter(
                            LocalDateTime.of(beginLocalDateTime.getYear(), beginLocalDateTime.getMonth(), beginLocalDateTime.getDayOfMonth(), 12, 0)
                                                                                             );

                    float staffNotClassesDay = arrangeWorkQueryService.getStaffNotWorkDay(employeeId, new Period(beginLocalDateTime, DateUtils.offset(beginLocalDateTime, ChronoUnit.HOURS, 12)), LeaveFormDetail.LeaveTimeUnit.AM);
                    if (isAM) {
                        besideAm = besideAm + amHour * staffNotClassesDay;
                    } else if (isPM) {
                        besidePm = besidePm + pmHour * staffNotClassesDay;
                    }
                    beginLocalDateTime = DateUtils.offset(beginLocalDateTime, ChronoUnit.HOURS, 12);
                    beginLocalDateTime = DateUtils.offsetMillisecond(beginLocalDateTime, 1);
                }
            }

            hour = hour - besideAm - besidePm;
        }
        if (leaveTime.getStartTime().getTimeUnit() == LeaveFormDetail.LeaveTimeUnit.DAY) {
            LeaveRule.LeaveTimeUnitConvert leaveTimeUnitConvert = leaveRule.getMinimumLeaveTimeUnitConvert().getLeaveTimeUnitConverts()
                    .stream().filter(convert -> convert instanceof LeaveRule.DayConvert).findFirst()
                    .orElse(new LeaveRule.LeaveTimeUnitConvert());//.get();
            LeaveRule.DayConvert dayConvert = (LeaveRule.DayConvert) leaveTimeUnitConvert;

            long day = DateUtils.between(leaveTime.getPeriod().getBegin(), leaveTime.getPeriod().getEnd(), ChronoUnit.DAYS, false) + 1;
            if (leaveRule.getLeaveTimeConvert().getConvertWay() == LeaveTimeConvert.ConvertWay.WORKING_DAY) {
                ArrangeWorkQueryService arrangeWorkQueryService = ApplicationContextHolder.getBean(ArrangeWorkQueryService.class);
                float staffNotClassesDay = arrangeWorkQueryService.getStaffNotWorkDay(employeeId, leaveTime.getPeriod(), LeaveFormDetail.LeaveTimeUnit.DAY);
                hour = dayConvert.getDayHour() * day - dayConvert.getDayHour() * staffNotClassesDay;
            } else {
                hour = (float) day * 8;
            }
        }
        if (leaveTime.getStartTime().getTimeUnit() == LeaveFormDetail.LeaveTimeUnit.TIME_RANGE) {
            long time = DateUtils.betweenMs(leaveTime.getPeriod().getBegin(), leaveTime.getPeriod().getEnd());
            if (leaveRule.getLeaveTimeConvert().getConvertWay() == LeaveTimeConvert.ConvertWay.WORKING_DAY) {
                ArrangeWorkQueryService arrangeWorkQueryService = ApplicationContextHolder.getBean(ArrangeWorkQueryService.class);
                long staffNotClassesTime = arrangeWorkQueryService.getStaffNotWorkTime(employeeId, leaveTime.getPeriod());
                hour = (float) Math.floor((double) (time - staffNotClassesTime) / (1000 * 60 * 60));
            } else {
                float ft = time / (float) (1000 * 60 * 60);
                hour = (float) ((int) ft);
                if (hour < ft) {
                    hour++;
                }
            }
        }
        //        1.按自然日计算：按选择时间段统计请假时长，不会去除班次设置的休息时间，选择多久就是请假多久；
        //        2.按工作日计算：按选择时间段计算请假时长，会识别请假时间范围内的班次，自动减去班次设置的休息时间。（仅针对假期单位为小时）
        //
        //        按选择的请假时间段计算假期时长,会识别请假时段中的班次信息，自动减去员工的休息日\法定节假日(假期单位为天或半天）
        return leaveRule.getLeaveTimeConvert().convert(hour);
    }
}
