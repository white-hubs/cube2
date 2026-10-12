package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.application.datasync;

import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.RenewWorkStatusBatchParam;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.application.datasync.constants.UserStatusConstants;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.entity.DingTalkSchedule;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.enumeration.CheckTypeEnum;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.valueobj.DingTalkUserRef;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.DingTalkScheduleRepository;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.DingTalkUserRefRepository;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DingTalkUserWorkStatusUpdateJob {
    private final DingTalkScheduleRepository scheduleRepository;
    private final UserClient userClient;
    private final DingTalkUserRefRepository dingTalkUserRefRepository;
    private final TerminalClient terminalClient;

    public DingTalkUserWorkStatusUpdateJob(DingTalkScheduleRepository scheduleRepository, UserClient userClient,
                                           DingTalkUserRefRepository dingTalkUserRefRepository, TerminalClient terminalClient
                                          ) {
        this.scheduleRepository = scheduleRepository;
        this.userClient = userClient;
        this.dingTalkUserRefRepository = dingTalkUserRefRepository;
        this.terminalClient = terminalClient;
    }

    /**
     * 系统启动时恢复所有用户工作状态
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void syncUserStatus() {
        SessionContextHolder.clearSessionContext();
        var now = LocalDateTime.now();
        log.info("开始同步用户工作状态，时间: {}", now);
        var allSchedules = getSchedules(now);
        if (CollectionUtils.isEmpty(allSchedules)) {
            return;
        }
        var tenantScheduleMap = allSchedules.stream().collect(Collectors.groupingBy(DingTalkSchedule::getTenant));
        tenantScheduleMap.forEach((tenant, value) -> {
            SessionContextHolder.requestTenant(tenant);
            // 按用户ID分组
            var userScheduleMap = value.stream().collect(Collectors.groupingBy(DingTalkSchedule::getUserid));
            var tenantUserRefMap = dingTalkUserRefRepository.getListByDingTalkUserIds(userScheduleMap.keySet()).stream()
                    .collect(Collectors.toMap(DingTalkUserRef::getDingTalkUserId, DingTalkUserRef::getSystemUserId));
            var userList = userClient.list(new UserQuery());
            var workUserIds = getWorkingUserIds(tenantUserRefMap, now, userScheduleMap);
            var allUserIds = userList.stream().map(UserDTO::getId).toList();
            var resetUserIds = allUserIds.stream().filter(x -> !workUserIds.contains(x)).toList();
            updateUsersStatus(tenant, workUserIds, resetUserIds);
        });
        log.info("用户工作状态更新完成");
    }

    /**
     * 获取用户的工作状态
     */
    private List<Long> getWorkingUserIds(Map<String, Long> tenantUserRefMap, LocalDateTime checkTime,
                                         Map<String, List<DingTalkSchedule>> schedulesMap
                                        ) {
        var userIds = new ArrayList<Long>();
        tenantUserRefMap.forEach((dingTalkUserId, userId) -> {
            var userSchedules = schedulesMap.getOrDefault(dingTalkUserId, Collections.emptyList());
            var status = determineWorkStatus(userSchedules, checkTime);
            var systemUserId = tenantUserRefMap.get(dingTalkUserId);
            if (UserStatusConstants.WORKING_STATUS.equals(status)) {
                userIds.add(systemUserId);
            }
        });
        return userIds;
    }

    public List<DingTalkSchedule> getSchedules(LocalDateTime checkTime) {
        var today = DateUtils.endOfDay(checkTime);
        var yesterday = DateUtils.beginOfDay(today.plusDays(-1));
        return scheduleRepository.getSchedules(yesterday, today);
    }

    /**
     * 判断用户工作状态
     */
    private String determineWorkStatus(List<DingTalkSchedule> schedules, LocalDateTime checkTime) {

        // 过滤无效排班
        var validSchedules = schedules.stream()
                .filter(s -> !"Y".equals(s.getIsRest()))
                .filter(s -> s.getShiftId() != null && s.getShiftId() != 0)
                .collect(Collectors.toList());

        if (validSchedules.isEmpty()) {
            return UserStatusConstants.NOT_WORKING_STATUS;
        }

        // 找出所有工作时间段
        var workPeriods = buildWorkPeriods(validSchedules);

        // 检查当前时间是否在任意工作时段内
        for (var period : workPeriods) {
            if (period.contains(checkTime)) {
                return UserStatusConstants.WORKING_STATUS;
            }
        }
        return UserStatusConstants.NOT_WORKING_STATUS;
    }

    /**
     * 构建工作时间段（处理跨天情况）
     */
    private List<TimePeriod> buildWorkPeriods(List<DingTalkSchedule> schedules) {
        // 按班次版本分组
        var byShift = schedules.stream()
                .collect(Collectors.groupingBy(DingTalkSchedule::getShiftVersion));
        var periods = new ArrayList<TimePeriod>();
        byShift.forEach((shiftVersion, shiftSchedules) -> {
            var checkIn = shiftSchedules.stream()
                    .filter(s -> CheckTypeEnum.OnDuty.equals(s.getCheckType()))
                    .map(DingTalkSchedule::getPlanCheckTime)
                    .min(Comparator.naturalOrder());

            var checkOut = shiftSchedules.stream()
                    .filter(s -> CheckTypeEnum.OffDuty.equals(s.getCheckType()))
                    .map(DingTalkSchedule::getPlanCheckTime)
                    .max(Comparator.naturalOrder());

            // 只有上班时间
            if (checkIn.isPresent() && checkOut.isPresent()) {
                // 处理跨天情况
                if (checkOut.get().isBefore(checkIn.get())) {
                    // 拆分为两个时间段
                    periods.add(new TimePeriod(
                            checkIn.get(),
                            checkIn.get().toLocalDate().atTime(LocalTime.MAX)
                    ));
                    periods.add(new TimePeriod(
                            checkOut.get().toLocalDate().atTime(LocalTime.MIN),
                            checkOut.get()
                    ));
                } else {
                    periods.add(new TimePeriod(checkIn.get(), checkOut.get()));
                }
            } else {
                checkIn.ifPresent(dateTime -> periods.add(new TimePeriod(
                        dateTime,
                        dateTime.toLocalDate().atTime(LocalTime.MAX)
                )));
            }
        });
        return periods;
    }

    /**
     * 更新用户状态
     */
    private void updateUsersStatus(Integer tenant, List<Long> workUserIds, List<Long> resetUserIds) {
        try {
            var renewWorkStatusBatchParam = new RenewWorkStatusBatchParam();
            renewWorkStatusBatchParam.setRestStaffList(resetUserIds);
            renewWorkStatusBatchParam.setWorkStaffList(workUserIds);
            renewWorkStatusBatchParam.setTenant(tenant);
            terminalClient.batchUpdateTenantUserWorkStatus(renewWorkStatusBatchParam);
        } catch (Exception e) {
            log.error("更新用户状态失败,工作用户:{},休息用户:{}", workUserIds, resetUserIds, e);
        }
    }

    /**
     * 工作时间段内部类
     */
    @Data
    @AllArgsConstructor
    private static class TimePeriod {
        private LocalDateTime start;
        private LocalDateTime end;

        public boolean contains(LocalDateTime time) {
            return !time.isBefore(start) && !time.isAfter(end);
        }
    }
}
