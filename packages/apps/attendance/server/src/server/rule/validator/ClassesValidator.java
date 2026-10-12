package com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupClassesCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitClassesCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.ClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WeekEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.ClassesPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassesValidator {

    private final ClassesMapper classesMapper;

    /**
     * 验证班次名称是否相同
     *
     * @param id   班次ID
     * @param name 名称
     */
    public void verifyIsExistByName(Long id, String name) {
        ClassesPO po = classesMapper.selectOne(Wrappers.<ClassesPO>lambdaQuery()
                                                       .eq(ClassesPO::getName, name)
                                                       .ne(null != id, ClassesPO::getId, id)
                                              );
        Assert.isConflict(Objects.nonNull(po), "存在相同名称!");
    }

    /**
     * 验证班次中时间是否有交叉
     *
     * @param command 提交名称
     */
    public void verifyIsCrossByDate(SubmitClassesCommand command) {
        // 获取所有的时间字符串
        String startWork = command.getStartWorkTime();
        String beforeStartWork = command.getBeforeStartWorkTime();
        String afterStartWork = command.getAfterStartWorkTime();
        String endWork = command.getEndWorkTime();
        String beforeEndWork = command.getBeforeEndWorkTime();
        String afterEndWork = command.getAfterEndWorkTime();
        String restStart = command.getRestStartTime();
        String restEnd = command.getRestEndTime();
        // 获取上班下班时间
        LocalDateTime startWorkTime = null;
        LocalDateTime endWorkTime = null;
        try {
            startWorkTime = DateUtils.parse(startWork, "HH:mm");
            endWorkTime = DateUtils.parse(endWork, "HH:mm");
        } catch (Exception e) {
            log.error("抓换时间失败", e);
        }
        Assert.isParameterError(startWorkTime == null || endWorkTime == null, "上下班时间不能为空");
        // 比较上下班时间
        int compare = DateUtils.compare(startWorkTime, endWorkTime);
        // 如果上班时间比下班时间晚，那么上班时间就是次日的
        if (compare > 0) {
            command.setEndTimeIsMorrow(true);
            endWorkTime = DateUtils.offsetDay(endWorkTime, 1);
        } else if (compare == 0) {
            throw new ServiceException("上班时间不能等于下班时间");
        } else {
            command.setEndTimeIsMorrow(false);
        }
        Assert.isParameterError(DateUtils.compare(startWorkTime, endWorkTime) >= 0, "上班时间不能晚于或等于下班时间");
        // 需要重新校验
        if (command.getEndTimeIsMorrow() && StringUtils.isNotEmpty(beforeStartWork)) {
            LocalDateTime beforeStartWorkTime = DateUtils.parse(beforeStartWork, "HH:mm");
            Assert.isParameterError(
                    DateUtils.compare(beforeStartWorkTime, startWorkTime) > 0, "上班打卡时间范围开始时间应在上班时间之前");
        }
        LocalDateTime afterStartWorkTime = null;
        if (StringUtils.isNotEmpty(afterStartWork)) {
            afterStartWorkTime = DateUtils.parse(afterStartWork, "HH:mm");
            if (command.getEndTimeIsMorrow() && afterStartWorkTime.isBefore(startWorkTime)) {
                afterStartWorkTime = DateUtils.offsetDay(afterStartWorkTime, 1);
            }
            Assert.isParameterError(
                    DateUtils.compare(afterStartWorkTime, startWorkTime) < 0, "上班打卡时间范围结束时间应在上班时间之后");
            Assert.isParameterError(DateUtils.compare(afterStartWorkTime, endWorkTime) >= 0, "上班打卡时间范围结束时间应在下班时间之前");
        }
        // 下班打卡范围的开始时间可能会在前一天，所以这里的判断还需完善
        // 如果下班范围开始时间比下班时间小，那么就是第二天，如果比下班时间晚那么就是前一天
        LocalDateTime beforeEndWorkTime = null;
        if (StringUtils.isNotEmpty(beforeEndWork)) {
            beforeEndWorkTime = DateUtils.parse(beforeEndWork, "HH:mm");
            if (command.getEndTimeIsMorrow() &&
                beforeEndWorkTime.isBefore(DateUtils.parse(endWork, "HH:mm"))) {
                beforeEndWorkTime = DateUtils.offsetDay(beforeEndWorkTime, 1);
            }
            Assert.isParameterError(DateUtils.compare(beforeEndWorkTime, endWorkTime) > 0, "下班打卡时间范围开始时间应在下班时间之前");
            Assert.isParameterError(DateUtils.compare(beforeEndWorkTime, startWorkTime) <= 0, "下班打卡时间范围开始时间应在上班时间之后");
        }
        if (StringUtils.isNotEmpty(afterEndWork)) {
            LocalDateTime afterEndWorkTime = DateUtils.parse(afterEndWork, "HH:mm");
            if (command.getEndTimeIsMorrow()) {
                afterEndWorkTime = DateUtils.offsetDay(afterEndWorkTime, 1);
            } else if (afterEndWorkTime.isBefore(DateUtils.parse(endWork, "HH:mm"))) {
                afterEndWorkTime = DateUtils.offsetDay(afterEndWorkTime, 1);
            }
            Assert.isParameterError(DateUtils.compare(afterEndWorkTime, endWorkTime) < 0, "下班打卡时间范围结束时间应在下班时间之后");
        }

        LocalDateTime restStartTime = null;
        if (StringUtils.isNotEmpty(restStart)) {
            restStartTime = DateUtils.parse(restStart, "HH:mm");
            if (command.getEndTimeIsMorrow() && DateUtils.compare(restStartTime, startWorkTime) < 0) {
                // 如果开始休息时间 晚于上班时间且是跨天的班次，休息开始时间就是第二天
                restStartTime = DateUtils.offsetDay(restStartTime, 1);
            }
            Assert.isParameterError(DateUtils.compare(restStartTime, startWorkTime) <= 0, "休息开始时间不能早于或等于上班时间");
        }
        LocalDateTime restEndTime = null;
        if (StringUtils.isNotEmpty(restEnd)) {
            restEndTime = DateUtils.parse(restEnd, "HH:mm");
            if (command.getEndTimeIsMorrow() && DateUtils.compare(restEndTime, startWorkTime) < 0) {
                // 如果结束休息时间 晚于上班时间且是跨天的班次，休息结束时间就是第二天
                restEndTime = DateUtils.offsetDay(restEndTime, 1);
            }
            Assert.isParameterError(DateUtils.compare(restEndTime, endWorkTime) >= 0, "休息结束时间不能晚于或等于下班时间");
        }
        if (restStartTime != null && restEndTime != null) {
            Assert.isParameterError(DateUtils.compare(restStartTime, restEndTime) >= 0, "休息开始时间不能晚于或等于休息结束时间");
        }
        if (afterStartWorkTime != null && beforeEndWorkTime != null) {
            Assert.isParameterError(
                    DateUtils.compare(afterStartWorkTime, beforeEndWorkTime) > 0, "上班打卡时间结束时间应在下班打卡开始时间之前");
        }
    }

    /**
     * 比较两个班次时间是否交叉
     *
     * @param classes1 班次1
     * @param classes2 班次2
     *
     * @return 返回结果
     * -1 班次1在班次2之前，即班次1的下班时间早于班次2的上班时间
     * 0 班次1和班次2存在交叉，交叉是指上下班时间交叉或者打卡范围交叉
     * 1 班次1在班次2之后，即班次1的上班时间晚于班次2的下班时间
     * 2 无法比较
     */
    public int compareTwoClasses(Classes classes1, Classes classes2) {
        if (classes1 == null || classes2 == null) {
            return 2;
        }
        if (ClassesMarkEnum.All_DAY_REST.name().equals(classes1.getMark()) ||
            ClassesMarkEnum.All_DAY_REST.name().equals(classes2.getMark())) {
            return 2;
        }
        // 得到所有的时间数据
        LocalDateTime startWorkDate1 = classes1.getTimeStrToDate(classes1.getStartWorkTime());
        LocalDateTime endWorkDate1 = classes1.getEndTimeStrToDate(classes1.getEndWorkTime());
        LocalDateTime beforeStartWorkDate1 = classes1.getTimeStrToDate(classes1.getBeforeStartWorkTime());
        LocalDateTime afterEndWorkDate1 = classes1.getEndTimeStrToDate(classes1.getAfterEndWorkTime());

        LocalDateTime startWorkDate2 = classes2.getTimeStrToDate(classes2.getStartWorkTime());
        LocalDateTime endWorkDate2 = classes2.getEndTimeStrToDate(classes2.getEndWorkTime());
        LocalDateTime beforeStartWorkDate2 = classes2.getTimeStrToDate(classes2.getBeforeStartWorkTime());
        LocalDateTime afterEndWorkDate2 = classes2.getEndTimeStrToDate(classes2.getAfterEndWorkTime());
        if (DateUtils.compare(endWorkDate1, startWorkDate2) < 0) {
            int compareClassDate = this.compareClassClockDate(endWorkDate1, afterEndWorkDate1, beforeStartWorkDate2, startWorkDate2);
            if (compareClassDate == 0) {
                return 0;
            }
            return -1;
        }
        if (DateUtils.compare(startWorkDate1, endWorkDate2) > 0) {
            int compareClassDate = this.compareClassClockDate(endWorkDate2, afterEndWorkDate2, beforeStartWorkDate1, startWorkDate1);
            if (compareClassDate == 0) {
                return 0;
            }
            return 1;
        }
        return 0;
    }

    /**
     * 比较两个班次的上下班打卡时间范围是否冲突
     *
     * @param endWorkDate1
     * @param afterEndWorkDate1
     * @param beforeStartWorkDate2
     * @param startWorkDate2
     *
     * @return 0/冲突；1/不冲突
     */
    private int compareClassClockDate(LocalDateTime endWorkDate1, LocalDateTime afterEndWorkDate1, LocalDateTime beforeStartWorkDate2,
                                      LocalDateTime startWorkDate2
                                     ) {
        if (afterEndWorkDate1 != null) {
            if (DateUtils.compare(afterEndWorkDate1, startWorkDate2) >= 0) {
                return 0;
            }
            if (beforeStartWorkDate2 != null && DateUtils.compare(afterEndWorkDate1, beforeStartWorkDate2) >= 0) {
                return 0;
            }
        }
        if (beforeStartWorkDate2 != null) {
            if (DateUtils.compare(endWorkDate1, beforeStartWorkDate2) >= 0) {
                return 0;
            }
            if (afterEndWorkDate1 != null && DateUtils.compare(afterEndWorkDate1, beforeStartWorkDate2) >= 0) {
                return 0;
            }
        }
        return 1;
    }

    /**
     * 校验考勤组一周的班次不能交叉
     *
     * @param classesCommands 考勤组班次
     *
     * @return
     */
    public void verifyIsCrossByWeek(List<SubmitAttendanceGroupClassesCommand> classesCommands) {
        classesCommands.forEach(cla -> {
            // 校验当天班次
            List<Classes> classes = cla.getClasses();
            Integer week = cla.getWeek();
            // 当天如果存在休息班次则不能有其他班次
            Assert.isConflict(!this.ifHaveRestClassesHaveAnyOther(classes),
                              WeekEnum.getWeekName(week) + "有休息班次就不能再有其他班次");
            // 当天的所有班次不能交叉
            Assert.isConflict(this.anyClassesNoCross(classes), WeekEnum.getWeekName(week) + "的班次有交叉(包括打卡范围)");

            // 校验前后一天的班次
            List<Classes> todayClasses = cla.getClasses();
            SubmitAttendanceGroupClassesCommand morrow = null;
            if (!week.equals(WeekEnum.SAT.getWeek())) {
                morrow = classesCommands.stream().filter(cl -> cl.getWeek().equals(week + 1)).findAny().orElse(null);
            } else {
                morrow = classesCommands.stream().filter(cl -> cl.getWeek().equals(WeekEnum.SUN.getWeek())).findAny().orElse(null);
            }
            if (morrow != null) {
                List<Classes> morrowClasses = morrow.getClasses();
                List<Classes> sortedClasses = morrowClasses.stream().filter(m -> !ClassesMarkEnum.All_DAY_REST.name().equals(m.getMark()))
                        .sorted(Comparator.comparing(m -> m.getTimeStrToDate(m.getStartWorkTime()))).toList();
                if (!CollectionUtils.isEmpty(sortedClasses)) {
                    Classes morrowEarlyClasses = sortedClasses.getFirst();
                    // 今日班次中有一个下班时间比明日班次的最早上班时间晚，则跨天班次存在交叉
                    Assert.isConflict(this.checkCross(todayClasses, morrowEarlyClasses),
                                      WeekEnum.getWeekName(week) + "与" + WeekEnum.getWeekName(morrow.getWeek()) +
                                      "的班次时间有交叉(包括打卡范围)");
                }
            }
        });
    }

    private Boolean checkCross(List<Classes> todayClasses, Classes morrowEarlyClasses) {
        if (CollectionUtils.isEmpty(todayClasses)) {
            return false;
        }
        return todayClasses.stream().noneMatch(
                today -> {
                    if (StringUtils.isEmpty(today.getEndWorkTime()) || StringUtils.isEmpty(morrowEarlyClasses.getStartWorkTime())) {
                        return false;
                    }
                    // 当天的结束时间
                    LocalDateTime todayEndTime = DateUtils.parse(today.getEndWorkTime(), "HH:mm");
                    // (有结束打卡范围就取范围)
                    if (StringUtils.isNotEmpty(today.getAfterEndWorkTime())) {
                        LocalDateTime afterTodayEndTime = DateUtils.parse(today.getAfterEndWorkTime(), "HH:mm");
                        // 如果标注跨天或者班次结束时间大于打卡范围的结束时间，说明时间跨天需要加一天
                        if ((today.getEndTimeIsMorrow() != null && today.getEndTimeIsMorrow()) ||
                            DateUtils.compare(todayEndTime, afterTodayEndTime) > 0) {
                            todayEndTime = DateUtils.offsetDay(afterTodayEndTime, 1);
                        } else {
                            todayEndTime = afterTodayEndTime;
                        }
                    }

                    LocalDateTime morrowStarTime = DateUtils.parse(morrowEarlyClasses.getStartWorkTime(), "HH:mm");
                    // 如果上班打卡有范围就取范围的开始时间
                    if (StringUtils.isNotEmpty(morrowEarlyClasses.getBeforeStartWorkTime())) {
                        LocalDateTime befStartTime = DateUtils.parse(morrowEarlyClasses.getBeforeStartWorkTime(), "HH:mm");
                        // （上班打卡范围开始时间，如果小于上班时间说明开始时间在前一天）因为是第二天的班次默认要加一天，
                        // 如果满足上面括号里面的条件需要减一天，就会抵消。所以综合一下就是如果不满足括号中的条件就(默认)加一天
                        if (DateUtils.compare(befStartTime, morrowStarTime) <= 0) {
                            morrowStarTime = DateUtils.offsetDay(befStartTime, 1);
                        } else {
                            morrowStarTime = befStartTime;
                        }
                    }

                    return DateUtils.compare(todayEndTime, morrowStarTime) >= 0;
                }

                                              );
    }

    /**
     * 如果有休息班次是否还有其他班次
     *
     * @param classes 班次列表
     *
     * @return 返回结果 true：有其他班次；  false：无其他班次
     */
    public boolean ifHaveRestClassesHaveAnyOther(List<Classes> classes) {
        boolean rest = classes.stream().anyMatch(cla -> ClassesMarkEnum.All_DAY_REST.name().equals(cla.getMark()));
        boolean anyOther = classes.stream().anyMatch(cla -> !ClassesMarkEnum.All_DAY_REST.name().equals(cla.getMark()));
        return rest && anyOther;
    }

    /**
     * 是否任何班次都不交叉(同一天的班次)
     *
     * @return 返回结果 true 不交叉； false  交叉
     */
    public boolean anyClassesNoCross(List<Classes> classes) {
        boolean flag = true;
        if (CollectionUtils.isEmpty(classes) || classes.size() == 1) {
            return flag;
        }
        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                int compareTwoClasses = this.compareTwoClasses(classes.get(i), classes.get(j));
                if (compareTwoClasses == 0) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 是否任何班次都不交叉(两天的班次)
     *
     * @param theDayClasses   当天的班次
     * @param tomorrowClasses 后一天的班次
     *
     * @return 返回结果 true 不交叉； false  交叉
     */
    public boolean anyClassesNoCross(List<Classes> theDayClasses, List<Classes> tomorrowClasses) {
        boolean flag = true;
        if (CollectionUtils.isEmpty(theDayClasses) || CollectionUtils.isEmpty(tomorrowClasses)) {
            return flag;
        }
        for (Classes theDay : theDayClasses) {
            Boolean endTimeIsMorrow = theDay.getEndTimeIsMorrow();
            if (null == endTimeIsMorrow || !endTimeIsMorrow) {
                continue;
            }
            LocalDateTime endWorkDate1 = theDay.getTimeStrToDate(theDay.getEndWorkTime());
            LocalDateTime afterEndWorkDate1 = theDay.getTimeStrToDate(theDay.getAfterEndWorkTime());
            for (Classes tomorrow : tomorrowClasses) {
                LocalDateTime beforeStartWorkDate2 = tomorrow.getTimeStrToDate(tomorrow.getBeforeStartWorkTime());
                LocalDateTime startWorkDate2 = tomorrow.getTimeStrToDate(tomorrow.getStartWorkTime());
                if (DateUtils.compare(endWorkDate1, startWorkDate2) >= 0) {
                    flag = false;
                    break;
                }
                int compareClassDate = this.compareClassClockDate(endWorkDate1, afterEndWorkDate1, beforeStartWorkDate2, startWorkDate2);
                if (compareClassDate == 0) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 当前时间和班次上下班时间比较
     *
     * @return -1 当前时间在班次时间之前
     * 0 当前时间在班次之中
     * 1 当前时间在班次之后
     */
    public int nowDateCompareClasses(String startWorkTimeStr, String endWorkTimeStr, Boolean endTimeIsMorrow) {
        // 考虑下班时间是次日的问题
        LocalDateTime startWorkTime = DateUtils.parse(startWorkTimeStr, "HH:mm");
        LocalDateTime endWorkTime = DateUtils.parse(endWorkTimeStr, "HH:mm");
        if (endTimeIsMorrow != null && endTimeIsMorrow) {
            endWorkTime = DateUtils.offsetDay(endWorkTime, 1);
        }
        LocalDateTime nowDate = DateUtils.parse(DateUtils.format(LocalDateTime.now(), "HH:mm"), "HH:mm");
        if (DateUtils.compare(startWorkTime, nowDate) > 0) {
            return -1;
        } else if (DateUtils.compare(endWorkTime, nowDate) < 0) {
            return 1;
        } else {
            return 0;
        }
    }

}
