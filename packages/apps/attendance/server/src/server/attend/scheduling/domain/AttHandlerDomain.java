package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.platform.base.util.DateUtils;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 这一个操作类，不要加入对数据库修改的方法，可以有查询(但是尽量不要有
 */
public class AttHandlerDomain {

    /**
     * 组装自己班次交换对象
     *
     * @param fromScheduling
     * @param toScheduling
     * @param fromDate
     * @param toDate
     *
     * @return
     */
    public static Map<LocalDateTime, AttSchedulingPO> handleSchedulingChang(AttSchedulingPO fromScheduling, AttSchedulingPO toScheduling,
                                                                            LocalDateTime fromDate, LocalDateTime toDate
                                                                           ) {
        // 组装交换班次
        long toStartDayLong = DateUtils.toTimestamp(DateUtils.beginOfDay(toDate));
        long fromStartDayLong = DateUtils.toTimestamp(DateUtils.beginOfDay(fromDate));
        // 组装交换班次
        AttSchedulingPO fromAddSchedulingPO;
        if (toScheduling != null && toScheduling.getClassesRealStartTime() != null) {
            fromAddSchedulingPO = AttSchedulingPO.builder().build();
            BeanUtils.copyProperties(toScheduling, fromAddSchedulingPO);

            fromAddSchedulingPO.setSchedulingMonth(DateUtils.format(fromDate, "yyyy-MM"));
            fromAddSchedulingPO.setSchedulingDate(fromDate);
            fromAddSchedulingPO.setId(IdWorker.getId());
            fromAddSchedulingPO.setUpdateTime(LocalDateTime.now());
            if (fromScheduling != null) {
                fromAddSchedulingPO.setStaffGroupId(fromScheduling.getStaffGroupId());
                fromAddSchedulingPO.setSchedulingRuleId(fromScheduling.getSchedulingRuleId());
                fromAddSchedulingPO.setClockRuleId(fromScheduling.getClockRuleId());
                fromAddSchedulingPO.setClockRead(fromScheduling.getClockRead());
                fromAddSchedulingPO.setStaffGroupRanges(fromScheduling.getStaffGroupRanges());
                fromAddSchedulingPO.setSchedulingWays(fromScheduling.getSchedulingWays());
                fromAddSchedulingPO.setStaffDeptName(fromScheduling.getStaffDeptName());
                fromAddSchedulingPO.setStaffGroupName(fromScheduling.getStaffGroupName());
                fromAddSchedulingPO.setHandScheduling(true);
            }

            long toStartTime = DateUtils.toTimestamp(toScheduling.getClassesRealStartTime()) - toStartDayLong;
            long toEndTime = DateUtils.toTimestamp(toScheduling.getClassesRealEndTime()) - toStartDayLong;
            long toStartBeforeTime = DateUtils.toTimestamp(toScheduling.getClassesBeforeStartTime()) - toStartDayLong;
            long toStartAfterTime = DateUtils.toTimestamp(toScheduling.getClassesAfterStartTime()) - toStartDayLong;
            long toEndBeforeTime = DateUtils.toTimestamp(toScheduling.getClassesBeforeEndTime()) - toStartDayLong;
            long toEndAfterTime = DateUtils.toTimestamp(toScheduling.getClassesAfterEndTime()) - toStartDayLong;
            Long toStartRestTime = null;
            Long toEndRestTime = null;
            if (toScheduling.getClassesRestStartTime() != null && toScheduling.getClassesRestEndTime() != null) {
                toStartRestTime = DateUtils.toTimestamp(toScheduling.getClassesRestStartTime()) - toStartDayLong;
                toEndRestTime = DateUtils.toTimestamp(toScheduling.getClassesRestEndTime()) - toStartDayLong;
            }
            fromAddSchedulingPO.setClassesRealStartTime(DateUtils.parse(fromStartDayLong + toStartTime));
            fromAddSchedulingPO.setClassesBeforeStartTime(DateUtils.parse(fromStartDayLong + toStartBeforeTime));
            fromAddSchedulingPO.setClassesAfterStartTime(DateUtils.parse(fromStartDayLong + toStartAfterTime));
            fromAddSchedulingPO.setClassesRealEndTime(DateUtils.parse(fromStartDayLong + toEndTime));
            fromAddSchedulingPO.setClassesBeforeEndTime(DateUtils.parse(fromStartDayLong + toEndBeforeTime));
            fromAddSchedulingPO.setClassesAfterEndTime(DateUtils.parse(fromStartDayLong + toEndAfterTime));
            if (toStartRestTime != null) {
                fromAddSchedulingPO.setClassesRealStartTime(DateUtils.parse(fromStartDayLong + toStartRestTime));
                fromAddSchedulingPO.setClassesRealEndTime(DateUtils.parse(fromStartDayLong + toEndRestTime));
            }
            fromAddSchedulingPO.setStarted(fromAddSchedulingPO.getClassesRealStartTime().isBefore(LocalDateTime.now()));
            fromAddSchedulingPO.setHandScheduling(true);
        } else {
            fromAddSchedulingPO = null;
        }

        AttSchedulingPO toAddSchedulingPO;
        if (fromScheduling != null && fromScheduling.getClassesRealStartTime() != null) {
            toAddSchedulingPO = AttSchedulingPO.builder().build();
            BeanUtils.copyProperties(fromScheduling, toAddSchedulingPO);

            toAddSchedulingPO.setSchedulingMonth(DateUtils.format(toDate, "yyyy-MM"));
            toAddSchedulingPO.setSchedulingDate(toDate);
            toAddSchedulingPO.setId(IdWorker.getId());
            toAddSchedulingPO.setUpdateTime(LocalDateTime.now());
            if (toScheduling != null) {
                toAddSchedulingPO.setStaffGroupId(toScheduling.getStaffGroupId());
                toAddSchedulingPO.setSchedulingRuleId(toScheduling.getSchedulingRuleId());
                toAddSchedulingPO.setClockRuleId(toScheduling.getClockRuleId());
                toAddSchedulingPO.setClockRead(toScheduling.getClockRead());
                toAddSchedulingPO.setStaffGroupRanges(toScheduling.getStaffGroupRanges());
                toAddSchedulingPO.setSchedulingWays(toScheduling.getSchedulingWays());
                toAddSchedulingPO.setStaffDeptName(toScheduling.getStaffDeptName());
                toAddSchedulingPO.setStaffGroupName(toScheduling.getStaffGroupName());
                toAddSchedulingPO.setHandScheduling(true);
            }
            long fromStartTime = DateUtils.toTimestamp(fromScheduling.getClassesRealStartTime()) - fromStartDayLong;
            long fromEndTime = DateUtils.toTimestamp(fromScheduling.getClassesRealEndTime()) - fromStartDayLong;
            long fromStartBeforeTime = DateUtils.toTimestamp(fromScheduling.getClassesBeforeStartTime()) - fromStartDayLong;
            long fromStartAfterTime = DateUtils.toTimestamp(fromScheduling.getClassesAfterStartTime()) - fromStartDayLong;
            long fromEndBeforeTime = DateUtils.toTimestamp(fromScheduling.getClassesBeforeEndTime()) - fromStartDayLong;
            long fromEndAfterTime = DateUtils.toTimestamp(fromScheduling.getClassesAfterEndTime()) - fromStartDayLong;
            Long fromStartRestTime = null;
            Long fromEndRestTime = null;
            if (fromScheduling.getClassesRestStartTime() != null && fromScheduling.getClassesRestEndTime() != null) {
                fromStartRestTime = DateUtils.toTimestamp(fromScheduling.getClassesRestStartTime()) - fromStartDayLong;
                fromEndRestTime = DateUtils.toTimestamp(fromScheduling.getClassesRestEndTime()) - fromStartDayLong;
            }

            toAddSchedulingPO.setClassesRealStartTime(DateUtils.parse(toStartDayLong + fromStartTime));
            toAddSchedulingPO.setClassesBeforeStartTime(DateUtils.parse(toStartDayLong + fromStartBeforeTime));
            toAddSchedulingPO.setClassesAfterStartTime(DateUtils.parse(toStartDayLong + fromStartAfterTime));

            toAddSchedulingPO.setClassesRealEndTime(DateUtils.parse(toStartDayLong + fromEndTime));
            toAddSchedulingPO.setClassesBeforeEndTime(DateUtils.parse(toStartDayLong + fromEndBeforeTime));
            toAddSchedulingPO.setClassesAfterEndTime(DateUtils.parse(toStartDayLong + fromEndAfterTime));
            if (fromStartRestTime != null) {
                toAddSchedulingPO.setClassesRealStartTime(DateUtils.parse(toStartDayLong + fromStartRestTime));
                toAddSchedulingPO.setClassesRealEndTime(DateUtils.parse(toStartDayLong + fromEndRestTime));
            }
            toAddSchedulingPO.setStarted(toAddSchedulingPO.getClassesRealStartTime().isBefore(LocalDateTime.now()));
            toAddSchedulingPO.setHandScheduling(true);
        } else {
            toAddSchedulingPO = null;
        }

        Map<LocalDateTime, AttSchedulingPO> rsMap = new HashMap<>();
        rsMap.put(fromDate, fromAddSchedulingPO);
        rsMap.put(toDate, toAddSchedulingPO);
        return rsMap;
    }

    /**
     * 组装自己和自己换班对象
     *
     * @param fromDuty
     * @param toDuty
     * @param fromDate
     * @param toDate
     *
     * @return
     */
    public static Map<LocalDateTime, AttDutyPO> handleDutyChang(AttDutyPO fromDuty, AttDutyPO toDuty, LocalDateTime fromDate,
                                                                LocalDateTime toDate
                                                               ) {
        // 组装交换班次
        long fromStartDayLong = DateUtils.toTimestamp(DateUtils.beginOfDay(fromDate));
        long toStartDayLong = DateUtils.toTimestamp(DateUtils.beginOfDay(toDate));
        // 组装交换班次
        AttDutyPO fromAddDutyPO;
        if (toDuty != null && toDuty.getClassesRealStartTime() != null) {
            fromAddDutyPO = AttDutyPO.builder().build();
            BeanUtils.copyProperties(toDuty, fromAddDutyPO);

            fromAddDutyPO.setDutyMonth(DateUtils.format(fromDate, "yyyy-MM"));
            fromAddDutyPO.setDutyDate(fromDate);
            fromAddDutyPO.setId(IdWorker.getId());
            fromAddDutyPO.setUpdateTime(LocalDateTime.now());

            long toStartTime = DateUtils.toTimestamp(toDuty.getClassesRealStartTime()) - toStartDayLong;
            long toEndTime = DateUtils.toTimestamp(toDuty.getClassesRealEndTime()) - toStartDayLong;
            fromAddDutyPO.setClassesRealStartTime(DateUtils.parse(fromStartDayLong + toStartTime));
            fromAddDutyPO.setClassesRealEndTime(DateUtils.parse(fromStartDayLong + toEndTime));
        } else {
            fromAddDutyPO = null;
        }

        AttDutyPO toAddDutyPO;
        if (fromDuty != null && fromDuty.getClassesRealStartTime() != null) {
            toAddDutyPO = AttDutyPO.builder().build();
            BeanUtils.copyProperties(fromDuty, toAddDutyPO);

            toAddDutyPO.setDutyMonth(DateUtils.format(toDate, "yyyy-MM"));
            toAddDutyPO.setDutyDate(toDate);
            toAddDutyPO.setId(IdWorker.getId());
            toAddDutyPO.setUpdateTime(LocalDateTime.now());

            long fromStartTime = DateUtils.toTimestamp(fromDuty.getClassesRealStartTime()) - fromStartDayLong;
            long fromEndTime = DateUtils.toTimestamp(fromDuty.getClassesRealEndTime()) - fromStartDayLong;
            toAddDutyPO.setClassesRealStartTime(DateUtils.parse(toStartDayLong + fromStartTime));
            toAddDutyPO.setClassesRealEndTime(DateUtils.parse(toStartDayLong + fromEndTime));
        } else {
            toAddDutyPO = null;
        }

        Map<LocalDateTime, AttDutyPO> rsMap = new HashMap<>();
        rsMap.put(fromDate, fromAddDutyPO);
        rsMap.put(toDate, toAddDutyPO);
        return rsMap;
    }

    /**
     * 组装休息排班
     *
     * @param user
     * @param dateTime
     * @param groupPO
     * @param rulePO
     *
     * @return
     */
    public static AttSchedulingPO handleRestScheduling(UserDTO user, LocalDateTime dateTime, AttGroupPO groupPO, AttSchedulingRulePO rulePO,
                                                       String operator
                                                      ) {
        if (groupPO == null) {
            groupPO = AttGroupPO.builder().build();
        }
        if (rulePO == null) {
            rulePO = AttSchedulingRulePO.builder().build();
        }
        AttSchedulingPO schedulingPO = AttSchedulingPO.builder()
                .id(IdWorker.getId())
                .schedulingMonth(DateUtils.format(dateTime, "yyyy-MM"))
                .schedulingDate(dateTime)
                .staffId(user.getId())
                .staffName(user.getName())
                .staffDeptId(user.getOrganization() == null ? null : user.getOrganization().getId())
                .staffDeptName(user.getOrganization() == null ? null : user.getOrganization().getName())
                .staffGroupId(groupPO.getId())
                .schedulingRuleId(groupPO.getSchedulingRuleId())
                .clockRuleId(groupPO.getClockRuleId())
                .classesName("休息")
                .classesNickName("休息")
                .handScheduling(true)
                .started(false)
                .build();
        if (dateTime.isBefore(LocalDateTime.now()) || dateTime.isEqual(LocalDateTime.now())) {
            schedulingPO.setStarted(true);
            schedulingPO.setStaffGroupName(groupPO.getName());
            schedulingPO.setStaffGroupRanges(groupPO.getRanges());
            schedulingPO.setSchedulingWays(rulePO.getWays());
        }
        schedulingPO.setCreateBy(operator);
        schedulingPO.setUpdateBy(operator);
        return schedulingPO;
    }
}
