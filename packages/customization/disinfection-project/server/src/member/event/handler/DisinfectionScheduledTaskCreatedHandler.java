package com.guhecloud.cube.srv.gen.disinfection.server.member.event.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.iot.north.bridge.client.IotProductMethodClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.MethodInvokeRequestDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.MethodInvokeResponseDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberScheduledTaskDevice;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberScheduledTaskPlan;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ManualControlCommandEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ManualControlMethodParamEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ScheduleModeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionScheduledTaskCreatedEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionScheduledTaskDeviceUnboundEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberScheduledTaskDeviceService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberScheduledTaskPlanService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberScheduledTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.utils.SnowflakeBase62Util;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 定时任务创建事件监听（当设备列表不为空时触发，后续可在此实现定时任务调度等逻辑）
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionScheduledTaskCreatedHandler {
    private final IotProductMethodClient iotProductMethodClient;
    private final DisinfectionMemberScheduledTaskService disinfectionMemberScheduledTaskService;
    private final DisinfectionMemberScheduledTaskDeviceService disinfectionMemberScheduledTaskDeviceService;
    private final DisinfectionMemberScheduledTaskPlanService disinfectionMemberScheduledTaskPlanService;

    @EventListener
    public void onScheduledTaskCreated(DisinfectionScheduledTaskCreatedEvent event) {
        var scheduledTaskId = event.getScheduledTaskId();
        var task = disinfectionMemberScheduledTaskService.getById(scheduledTaskId);
        Assert.isParameterError(task == null, "定时任务不存在");

        var deviceList = disinfectionMemberScheduledTaskDeviceService.lambdaQuery()
                .eq(MemberScheduledTaskDevice::getScheduledTaskId, scheduledTaskId)
                .list();
        var planList = disinfectionMemberScheduledTaskPlanService.lambdaQuery()
                .eq(MemberScheduledTaskPlan::getScheduledTaskId, scheduledTaskId)
                .list();
        Assert.isParameterError(CollectionUtils.isEmpty(deviceList), "定时任务关联设备不能为空");

        Assert.isParameterError(task.getScheduleMode() == null, "定时任务模式不能为空");
        var scheduleMode = task.getScheduleMode();
        var mode = scheduleMode.getValue();

        var requests = ListUtils.convertItem(deviceList, d -> {
            var request = new MethodInvokeRequestDTO();
            request.setDeviceId(String.valueOf(d.getDeviceId()));
            request.setMethodCode(ManualControlCommandEnum.SCHEDULED_TASK.getMethodCode());
            request.setCommandSeq(IdWorker.getIdStr());

            var inputParams = new HashMap<String, Object>();
            inputParams.put(ManualControlMethodParamEnum.PLAN_MODE.getCode(), mode);
            inputParams.put(ManualControlMethodParamEnum.PLAN_SCHEDULE.getCode(), buildTTable(scheduleMode, planList));
            request.setInputParams(inputParams);
            return request;
        });
        var responses = iotProductMethodClient.batchInvokeMethod(requests);
        var deviceIds = ListUtils.convertItem(deviceList, MemberScheduledTaskDevice::getDeviceId);
        checkBatchInvokeResponses(responses, deviceIds, "定时任务下发");
    }

    @EventListener
    public void onScheduledTaskDeviceUnbound(DisinfectionScheduledTaskDeviceUnboundEvent event) {
        var deviceIds = event.getDeviceIds();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }

        var mode = ScheduleModeEnum.DAILY.getValue();
        var emptySchedule = "[[0,0]]";
        var requests = ListUtils.convertItem(deviceIds, deviceId -> {
            var request = new MethodInvokeRequestDTO();
            request.setDeviceId(String.valueOf(deviceId));
            request.setMethodCode(ManualControlCommandEnum.SCHEDULED_TASK.getMethodCode());
            request.setCommandSeq(IdWorker.getIdStr());

            var inputParams = new HashMap<String, Object>();
            inputParams.put(ManualControlMethodParamEnum.PLAN_MODE.getCode(), mode);
            inputParams.put(ManualControlMethodParamEnum.PLAN_SCHEDULE.getCode(), emptySchedule);
            request.setInputParams(inputParams);
            return request;
        });
        var responses = iotProductMethodClient.batchInvokeMethod(requests);
        checkBatchInvokeResponses(responses, deviceIds, "定时任务清空下发");
    }

    private static final Set<String> FAILED_STATUS = Set.of("REJECT", "FAIL", "EXPIRED");

    private void checkBatchInvokeResponses(List<MethodInvokeResponseDTO> responses, List<Long> deviceIds, String actionDesc) {
        if (CollectionUtils.isEmpty(responses)) {
            return;
        }
        var ids = ListUtils.orElse(deviceIds, List::of);
        var successDeviceIds = new ArrayList<Long>();
        var failedDeviceIds = new ArrayList<Long>();
        var failedDetails = new ArrayList<String>();
        for (var i = 0; i < responses.size(); i++) {
            var r = responses.get(i);
            var deviceId = i < ids.size() ? ids.get(i) : null;
            var statusCode = r != null ? r.getStatusCode() : null;
            var isFailed = statusCode != null && FAILED_STATUS.contains(statusCode);
            if (isFailed) {
                if (deviceId != null) {
                    failedDeviceIds.add(deviceId);
                }
                failedDetails.add("设备" + deviceId + ":" + statusCode + (r != null && r.getMessage() != null ? " " + r.getMessage() : ""));
            } else {
                if (deviceId != null) {
                    successDeviceIds.add(deviceId);
                }
            }
        }
        log.info("{}成功设备：{}", actionDesc, successDeviceIds);
        log.info("{}失败设备：{}，详情：{}", actionDesc, failedDeviceIds, failedDetails);
        if (CollectionUtils.isNotEmpty(failedDeviceIds)) {
            Assert.isParameterError(true, actionDesc + "失败");
        }
    }

    private String buildTTable(ScheduleModeEnum scheduleMode, List<MemberScheduledTaskPlan> planList) {
        var result = new ArrayList<String>();
        if (CollectionUtils.isEmpty(planList)) {
            var emptyRow = toRowString(List.of(0, 0));
            if (scheduleMode == ScheduleModeEnum.DAILY) {
                result.add(emptyRow);
                return "[" + String.join(",", result) + "]";
            }
            for (var day = 1; day <= 7; day++) {
                result.add(emptyRow);
            }
            return "[" + String.join(",", result) + "]";
        }
        if (scheduleMode == ScheduleModeEnum.DAILY) {
            result.add(toRowString(buildOneDayTable(planList)));
            return "[" + String.join(",", result) + "]";
        }

        for (var day = 1; day <= 7; day++) {
            var dayOfWeek = day;
            var oneDayPlans = planList.stream()
                    .filter(p -> p != null && p.getDayOfWeek() != null && p.getDayOfWeek() == dayOfWeek)
                    .collect(Collectors.toList());
            result.add(toRowString(buildOneDayTable(oneDayPlans)));
        }
        return "[" + String.join(",", result) + "]";
    }

    private String toRowString(List<Integer> row) {
        return row.stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]"));
    }

    private List<Integer> buildOneDayTable(List<MemberScheduledTaskPlan> plans) {
        if (CollectionUtils.isEmpty(plans)) {
            return List.of(0, 0);
        }
        plans.sort((a, b) -> Integer.compare(toMinute(a.getStartTime()), toMinute(b.getStartTime())));

        var row = new ArrayList<Integer>();
        row.add(0);
        row.add(0);

        for (var plan : plans) {
            Assert.isParameterError(plan.getCommand() == null, "定时任务计划指令不能为空");
            Assert.isParameterError(plan.getStartTime() == null, "定时任务计划开始时间不能为空");
            var startMinute = toMinute(plan.getStartTime());

            var cmdValue = toCommandValue(plan.getCommand());
            row.add(startMinute);
            row.add(cmdValue);

            if (cmdValue != 0) {
                Assert.isParameterError(plan.getEndTime() == null, "定时任务计划结束时间不能为空");
                var endMinute = toMinute(plan.getEndTime());
                row.add(endMinute);
                row.add(0);
            }
        }
        return row;
    }

    private int toMinute(String time) {
        Assert.isParameterError(time == null || !time.contains(":"), "时间格式错误");
        var parts = time.split(":", -1);
        Assert.isParameterError(parts.length != 2, "时间格式错误");
        int hour;
        int minute;
        try {
            hour = Integer.parseInt(parts[0]);
            minute = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            Assert.isParameterError(true, "时间格式错误");
            return 0;
        }
        Assert.isParameterError(hour < 0 || hour > 23 || minute < 0 || minute > 59, "时间格式错误");
        return hour * 60 + minute;
    }

    private int toCommandValue(ManualControlCommandEnum command) {
        if (command == ManualControlCommandEnum.DEVICE_OFF) {
            return 0;
        }
        if (command == ManualControlCommandEnum.DRIVE_MOSQUITO) {
            return 1;
        }
        if (command == ManualControlCommandEnum.MIST_SPRAY) {
            return 2;
        }
        Assert.isParameterError(true, "定时任务计划指令仅支持设备关闭、驱蚊指令、雾灌指令");
        return 0;
    }
}
