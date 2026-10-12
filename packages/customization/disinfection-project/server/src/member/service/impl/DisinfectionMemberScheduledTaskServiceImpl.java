package com.guhecloud.cube.srv.gen.disinfection.server.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.MemberScheduledTaskMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberScheduledTask;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberScheduledTaskDevice;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberScheduledTaskPlan;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ScheduleModeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionScheduledTaskCreatedEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionScheduledTaskDeviceUnboundEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ScheduledTaskCreateParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ScheduledTaskPlanItemParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ScheduledTaskQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ScheduledTaskUpdateParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberScheduledTaskDeviceService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberScheduledTaskPlanService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberScheduledTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ScheduledTaskDetailResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ScheduledTaskPageResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ScheduledTaskPlanItemResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberScheduledTaskServiceImpl extends ServiceImpl<MemberScheduledTaskMapper, MemberScheduledTask> implements DisinfectionMemberScheduledTaskService {

    private final DisinfectionMemberScheduledTaskDeviceService memberScheduledTaskDeviceService;
    private final DisinfectionMemberScheduledTaskPlanService memberScheduledTaskPlanService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DeviceCenterStdClient deviceCenterStdClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createScheduledTask(ScheduledTaskCreateParam param) {
        validatePlanTimeRanges(param.getPlanList(), param.getScheduleMode());
        validateDevicesNotBound(param.getDeviceIds(), null);

        var task = new MemberScheduledTask();
        task.setId(IdWorker.getId());
        task.setGroupId(param.getGroupId());
        task.setName(param.getName());
        task.setScheduleMode(param.getScheduleMode());
        save(task);
        var taskId = task.getId();

        if (CollectionUtils.isNotEmpty(param.getDeviceIds())) {
            var deviceList = ListUtils.convertItem(param.getDeviceIds(), deviceId -> {
                var po = new MemberScheduledTaskDevice();
                po.setId(IdWorker.getId());
                po.setDeviceId(deviceId);
                po.setScheduledTaskId(taskId);
                return po;
            });
            memberScheduledTaskDeviceService.saveBatch(deviceList);
        }

        if (CollectionUtils.isNotEmpty(param.getPlanList())) {
            var planList = new ArrayList<MemberScheduledTaskPlan>();
            for (ScheduledTaskPlanItemParam item : param.getPlanList()) {
                var plan = new MemberScheduledTaskPlan();
                plan.setId(IdWorker.getId());
                plan.setScheduledTaskId(taskId);
                plan.setCommand(item.getCommand());
                plan.setStartTime(item.getStartTime());
                plan.setEndTime(item.getEndTime());
                plan.setDayOfWeek(item.getDayOfWeek());
                planList.add(plan);
            }
            memberScheduledTaskPlanService.saveBatch(planList);
        }

        if (CollectionUtils.isNotEmpty(param.getDeviceIds())) {
            applicationEventPublisher.publishEvent(new DisinfectionScheduledTaskCreatedEvent(this, taskId));
        }

        return taskId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateScheduledTask(ScheduledTaskUpdateParam param) {
        var task = getById(param.getId());
        Assert.isParameterError(task == null, "定时任务不存在");
        var taskId = param.getId();

        validatePlanTimeRanges(param.getPlanList(), param.getScheduleMode());
        validateDevicesNotBound(param.getDeviceIds(), taskId);

        task.setGroupId(param.getGroupId());
        task.setName(param.getName());
        task.setScheduleMode(param.getScheduleMode());
        updateById(task);

        var oldDevices = memberScheduledTaskDeviceService.lambdaQuery()
                .eq(MemberScheduledTaskDevice::getScheduledTaskId, taskId)
                .list();
        var oldDeviceIds = ListUtils.convertItem(oldDevices, MemberScheduledTaskDevice::getDeviceId);
        var newDeviceIds = ListUtils.orElse(param.getDeviceIds(), Collections::emptyList);

        var oldDeviceIdSet = new HashSet<>(oldDeviceIds);
        var newDeviceIdSet = new HashSet<>(newDeviceIds);
        var unboundDeviceIdSet = new HashSet<>(oldDeviceIdSet);
        unboundDeviceIdSet.removeAll(newDeviceIdSet);
        var unboundDeviceIds = new ArrayList<>(unboundDeviceIdSet);

        // 先删除该任务下所有旧的设备关联与计划，再写入新数据
        if (CollectionUtils.isNotEmpty(oldDevices)) {
            var oldDevicePkIds = ListUtils.convertItem(oldDevices, MemberScheduledTaskDevice::getId);
            memberScheduledTaskDeviceService.removeByIds(oldDevicePkIds);
        }
        var oldPlanRecords = memberScheduledTaskPlanService.lambdaQuery()
                .eq(MemberScheduledTaskPlan::getScheduledTaskId, taskId)
                .list();
        if (CollectionUtils.isNotEmpty(oldPlanRecords)) {
            var oldPlanPkIds = ListUtils.convertItem(oldPlanRecords, MemberScheduledTaskPlan::getId);
            memberScheduledTaskPlanService.removeByIds(oldPlanPkIds);
        }

        if (CollectionUtils.isNotEmpty(newDeviceIds)) {
            var deviceList = ListUtils.convertItem(newDeviceIds, deviceId -> {
                var po = new MemberScheduledTaskDevice();
                po.setId(IdWorker.getId());
                po.setDeviceId(deviceId);
                po.setScheduledTaskId(taskId);
                return po;
            });
            memberScheduledTaskDeviceService.saveBatch(deviceList);
        }
        if (CollectionUtils.isNotEmpty(param.getPlanList())) {
            var planList = new ArrayList<MemberScheduledTaskPlan>();
            for (ScheduledTaskPlanItemParam item : param.getPlanList()) {
                var plan = new MemberScheduledTaskPlan();
                plan.setId(IdWorker.getId());
                plan.setScheduledTaskId(taskId);
                plan.setCommand(item.getCommand());
                plan.setStartTime(item.getStartTime());
                plan.setEndTime(item.getEndTime());
                plan.setDayOfWeek(item.getDayOfWeek());
                planList.add(plan);
            }
            memberScheduledTaskPlanService.saveBatch(planList);
        }

        if (CollectionUtils.isNotEmpty(unboundDeviceIds)) {
            applicationEventPublisher.publishEvent(new DisinfectionScheduledTaskDeviceUnboundEvent(this, taskId, unboundDeviceIds));
        }
        if (CollectionUtils.isNotEmpty(newDeviceIds)) {
            applicationEventPublisher.publishEvent(new DisinfectionScheduledTaskCreatedEvent(this, taskId));
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteScheduledTask(Long id) {
        Assert.isParameterError(id == null, "定时任务id不能为空");
        var deviceRecords = memberScheduledTaskDeviceService.lambdaQuery()
                .eq(MemberScheduledTaskDevice::getScheduledTaskId, id)
                .list();
        var deviceIds = ListUtils.convertItem(deviceRecords, MemberScheduledTaskDevice::getDeviceId);

        var planRecords = memberScheduledTaskPlanService.lambdaQuery()
                .eq(MemberScheduledTaskPlan::getScheduledTaskId, id)
                .list();

        if (CollectionUtils.isNotEmpty(planRecords)) {
            var planPkIds = ListUtils.convertItem(planRecords, MemberScheduledTaskPlan::getId);
            memberScheduledTaskPlanService.removeByIds(planPkIds);
        }
        if (CollectionUtils.isNotEmpty(deviceRecords)) {
            var devicePkIds = ListUtils.convertItem(deviceRecords, MemberScheduledTaskDevice::getId);
            memberScheduledTaskDeviceService.removeByIds(devicePkIds);
        }

        var deleted = removeById(id);
        if (deleted && CollectionUtils.isNotEmpty(deviceIds)) {
            applicationEventPublisher.publishEvent(new DisinfectionScheduledTaskDeviceUnboundEvent(this, id, deviceIds));
        }
        return deleted;
    }

    @Override
    public ScheduledTaskDetailResult getScheduledTaskDetail(Long id) {
        Assert.isParameterError(id == null, "定时任务id不能为空");
        var task = getById(id);
        Assert.isParameterError(task == null, "定时任务不存在");
        var result = new ScheduledTaskDetailResult();
        result.setId(task.getId());
        result.setGroupId(task.getGroupId());
        result.setName(task.getName());
        result.setScheduleMode(task.getScheduleMode());
        var devices = memberScheduledTaskDeviceService.lambdaQuery().eq(MemberScheduledTaskDevice::getScheduledTaskId, id).list();
        var deviceIds = ListUtils.convertItem(devices, MemberScheduledTaskDevice::getDeviceId);
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            var postParam = new DevicePostByIdSetParam();
            postParam.setDeviceIdSet(new HashSet<>(deviceIds));
            var deviceDetails = deviceCenterStdClient.postDetailsListByDeviceId(postParam);
            var deviceMap = MapUtils.listToMap(ListUtils.orElse(deviceDetails, List::of), DeviceBaseDetailsDTO::getId, d -> d);
            result.setDeviceList(ListUtils.convertItem(deviceIds, deviceMap::get));
        } else {
            result.setDeviceList(List.of());
        }
        var plans = memberScheduledTaskPlanService.lambdaQuery().eq(MemberScheduledTaskPlan::getScheduledTaskId, id).list();
        result.setPlanList(ListUtils.convertItem(plans, p -> {
            var item = new ScheduledTaskPlanItemResult();
            item.setCommand(p.getCommand());
            item.setStartTime(p.getStartTime());
            item.setEndTime(p.getEndTime());
            item.setDayOfWeek(p.getDayOfWeek());
            return item;
        }));
        return result;
    }

    @Override
    public Pagination<ScheduledTaskPageResult> scheduledTaskPage(ScheduledTaskQueryParam param) {
        var wrapper = Wrappers.<MemberScheduledTask>lambdaQuery()
                .eq(param.getGroupId() != null, MemberScheduledTask::getGroupId, param.getGroupId())
                .like(StringUtils.isNotBlank(param.getName()), MemberScheduledTask::getName, param.getName())
                .eq(param.getScheduleMode() != null, MemberScheduledTask::getScheduleMode, param.getScheduleMode())
                .orderByDesc(MemberScheduledTask::getCreateTime);
        var page = page(PagingUtils.convert(param), wrapper);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var taskIds = ListUtils.convertItem(page.getRecords(), MemberScheduledTask::getId);
        var taskToDeviceIds = MapUtils.listGroupToMap(
                memberScheduledTaskDeviceService.lambdaQuery()
                        .in(MemberScheduledTaskDevice::getScheduledTaskId, taskIds)
                        .list(),
                MemberScheduledTaskDevice::getScheduledTaskId
                                                     );
        Set<Long> allDeviceIds = new HashSet<>();
        taskToDeviceIds.values().forEach(devices -> devices.forEach(d -> allDeviceIds.add(d.getDeviceId())));
        var deviceIdToName = new HashMap<Long, String>();
        if (CollectionUtils.isNotEmpty(allDeviceIds)) {
            var postParam = new DevicePostByIdSetParam();
            postParam.setDeviceIdSet(allDeviceIds);
            var details = deviceCenterStdClient.postDetailsListByDeviceId(postParam);
            for (var d : ListUtils.orElse(details, List::of)) {
                if (d != null && d.getId() != null) {
                    deviceIdToName.put(d.getId(), d.getDeviceName() != null ? d.getDeviceName() : "");
                }
            }
        }
        var list = ListUtils.convertItem(page.getRecords(), task -> {
            var row = new ScheduledTaskPageResult();
            row.setId(task.getId());
            row.setGroupId(task.getGroupId());
            row.setName(task.getName());
            row.setScheduleMode(task.getScheduleMode());
            var devices = taskToDeviceIds.getOrDefault(task.getId(), List.of());
            row.setDeviceNames(ListUtils.convertItem(devices, d -> deviceIdToName.getOrDefault(d.getDeviceId(), "")));
            return row;
        });
        return PagingUtils.convert(page, list);
    }

    /**
     * 校验同一天的时间段不能交叉或重叠
     */
    private void validatePlanTimeRanges(List<ScheduledTaskPlanItemParam> planList, ScheduleModeEnum scheduleMode) {
        if (CollectionUtils.isEmpty(planList)) {
            return;
        }
        Assert.isParameterError(scheduleMode == null, "定时任务模式不能为空");
        for (int i = 0; i < planList.size(); i++) {
            var plan = planList.get(i);
            var planDesc = buildPlanDescription(i, plan, scheduleMode);
            if (scheduleMode == ScheduleModeEnum.WEEKLY) {
                Assert.isParameterError(plan.getDayOfWeek() == null,
                                        planDesc + "日期不能为空");
                Assert.isParameterError(plan.getDayOfWeek() < 1 || plan.getDayOfWeek() > 7,
                                        planDesc + "日期无效");
            }
            var start = parseTimeToMinute(plan.getStartTime(), "开始时间", planDesc);
            var end = parseTimeToMinute(plan.getEndTime(), "结束时间", planDesc);
            Assert.isParameterError(start >= end,
                                    planDesc + "开始时间需早于结束时间");
        }
        Map<Integer, List<ScheduledTaskPlanItemParam>> dayToPlans;
        if (scheduleMode == ScheduleModeEnum.DAILY) {
            dayToPlans = Map.of(1, planList);
        } else {
            dayToPlans = MapUtils.listGroupToMap(planList, ScheduledTaskPlanItemParam::getDayOfWeek);
        }
        for (var plans : dayToPlans.values()) {
            if (CollectionUtils.isEmpty(plans)) {
                continue;
            }
            var sortedPlans = new ArrayList<>(plans);
            sortedPlans.sort((a, b) -> Integer.compare(parseTimeToMinute(a.getStartTime(), "开始时间", "定时任务计划"),
                                                       parseTimeToMinute(b.getStartTime(), "开始时间", "定时任务计划")));
            for (var i = 0; i < sortedPlans.size() - 1; i++) {
                var prev = sortedPlans.get(i);
                var next = sortedPlans.get(i + 1);
                var endPrev = parseTimeToMinute(prev.getEndTime(), "结束时间", "定时任务计划");
                var startNext = parseTimeToMinute(next.getStartTime(), "开始时间", "定时任务计划");
                Assert.isParameterError(endPrev >= startNext,
                                        buildDayDescription(scheduleMode, prev.getDayOfWeek())
                                                + "时间段重叠：" + prev.getStartTime() + "-" + prev.getEndTime()
                                                + "、" + next.getStartTime() + "-" + next.getEndTime());
            }
        }
    }

    /**
     * 校验设备未被其他定时任务绑定
     *
     * @param deviceIds     待校验的设备id列表
     * @param excludeTaskId 排除的定时任务id（更新时传入当前任务id，创建时传null）
     */
    private void validateDevicesNotBound(List<Long> deviceIds, Long excludeTaskId) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        var query = memberScheduledTaskDeviceService.lambdaQuery()
                .in(MemberScheduledTaskDevice::getDeviceId, deviceIds);
        if (excludeTaskId != null) {
            query.ne(MemberScheduledTaskDevice::getScheduledTaskId, excludeTaskId);
        }
        var bound = query.list();
        if (CollectionUtils.isEmpty(bound)) {
            return;
        }
        var boundDeviceIds = ListUtils.convertItem(bound, MemberScheduledTaskDevice::getDeviceId);
        Assert.isConflict(CollectionUtils.isNotEmpty(boundDeviceIds),
                          "设备已绑定定时任务：" + boundDeviceIds);
    }

    private int parseTimeToMinute(String time, String fieldName, String planDesc) {
        if (StringUtils.isBlank(time) || !time.contains(":")) {
            Assert.isParameterError(true, planDesc + fieldName + "格式应为HH:mm");
        }
        var parts = time.split(":", -1);
        Assert.isParameterError(parts.length != 2, planDesc + fieldName + "格式应为HH:mm");
        int hour;
        int minute;
        try {
            hour = Integer.parseInt(parts[0]);
            minute = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            Assert.isParameterError(true, planDesc + fieldName + "格式应为HH:mm");
            return 0;
        }
        Assert.isParameterError(hour < 0 || hour > 23 || minute < 0 || minute > 59,
                                planDesc + fieldName + "超出范围");
        return hour * 60 + minute;
    }

    private String buildPlanDescription(int index, ScheduledTaskPlanItemParam plan, ScheduleModeEnum scheduleMode) {
        var commandName = plan.getCommand() == null ? "未选择指令" : plan.getCommand().getName();
        return "第" + (index + 1) + "条计划（" + buildDayDescription(scheduleMode, plan.getDayOfWeek()) + "，" + commandName + "）";
    }

    private String buildDayDescription(ScheduleModeEnum scheduleMode, Integer dayOfWeek) {
        if (scheduleMode == ScheduleModeEnum.DAILY) {
            return "每天";
        }
        if (dayOfWeek == null) {
            return "每周";
        }
        return switch (dayOfWeek) {
            case 1 -> "周一";
            case 2 -> "周二";
            case 3 -> "周三";
            case 4 -> "周四";
            case 5 -> "周五";
            case 6 -> "周六";
            case 7 -> "周日";
            default -> "每周";
        };
    }
}
