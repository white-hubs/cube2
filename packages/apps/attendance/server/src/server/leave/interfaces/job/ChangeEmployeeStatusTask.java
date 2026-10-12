package com.guhecloud.cube.srv.gen.attendance.management.server.leave.interfaces.job;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Sets;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.ClockCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command.UpdateClockCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.event.ApprovedLeaveFormEvent;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.mapper.LeaveFormMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.stat.commandgateway.AttendanceStatServiceImpl;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.ChangeWorkingStatusCommand;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.WorkingStatus;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeEmployeeStatusTask {

    private final LeaveFormMapper leaveFormMapper;
    private final ClockCommandService clockCommandService;
    private final StaffClient staffClient;
    private final AttendanceStatServiceImpl attendanceStatService;

    private static final String waitingOfChangeStaffStatusKey = "waitingOfChangeStaffStatusList:";

    private final RedisTemplate<String, Object> redisTemplate;

    @EventListener
    public void save(final ApprovedLeaveFormEvent approvedLeaveFormEvent) {
        redisTemplate.opsForValue()
                .set(waitingOfChangeStaffStatusKey + approvedLeaveFormEvent.getLeaveFormId(), approvedLeaveFormEvent.getLeaveFormId());
    }

    /**
     * 更新因为请假导致的工作状态
     *
     * @param param
     *
     * @return
     */
    public void execute(String param) {
        var keys = redisTemplate.keys(waitingOfChangeStaffStatusKey + "*");
        List<Long> leaveFormIds = Objects.requireNonNull(redisTemplate.opsForValue().multiGet(keys))
                .stream()
                .map(x -> Long.valueOf(x.toString()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(leaveFormIds)) {
            return;
        }
        // 扫描满足当前时间大于等于请假开始时间的请假单
        var startLeaveForms = leaveFormMapper.selectListStartLeaveAndIgnoreTenant(Sets.newHashSet(leaveFormIds));
        // 修改员工工作状态和班次状态为为请假
        if (CollectionUtils.isNotEmpty(startLeaveForms)) {
            log.info("修改进入请假开始时间的请假单的员工工作状态和班次状态改为请假");
            //修改工作状态
            List<ChangeWorkingStatusCommand> workingStatusCommands = new ArrayList<>();
            startLeaveForms.forEach(leaveFormPO -> {
                var leaveForm = JsonUtils.deserialize(leaveFormPO.getLeaveFormDetail(), LeaveForm.class);
                SessionContextHolder.requestTenant(leaveFormPO.getTenant());
                var changeWorkingStatusCommand = ChangeWorkingStatusCommand.builder()
                        .workingStatus(WorkingStatus.ASK_LEAVE)
                        .employeeId(leaveForm.getDetail().getUserId())
                        .build();
                workingStatusCommands.add(changeWorkingStatusCommand);

                // 调整班次状态
                var updateClockCommand = new UpdateClockCommand();
                updateClockCommand.setStaffId(leaveForm.getDetail().getUserId());
                updateClockCommand.setPeriod(leaveForm.getDetail().getLeaveTime().getPeriod());
                updateClockCommand.setTrigger(ClassesStatusTriggerEnum.LEAVE.name());
                clockCommandService.updateClockToNormalByPeriod(
                        updateClockCommand
                                                               );
                redisTemplate.delete(waitingOfChangeStaffStatusKey + leaveFormPO.getId());
                // 更新人员统计信息
                var period = new Period(DateUtils.beginOfMonth(DateUtils.parseDate(updateClockCommand.getClockDate())), DateUtils.endOfMonth(DateUtils.parseDate(updateClockCommand.getClockDate())));
                attendanceStatService.staffStat(period, updateClockCommand.getStaffId(), leaveFormPO.getTenant());
            });
            staffClient.changeWorkingStatus(workingStatusCommands);
        }
        // 扫描当前时间大于等于结束时间
        var endOfLeaveForms = leaveFormMapper.selectListEndLeaveAndIgnoreTenant(Sets.newHashSet(leaveFormIds));
        if (CollectionUtils.isNotEmpty(endOfLeaveForms)) {
            //修改员工工作状态和班次状态为为正常
            List<ChangeWorkingStatusCommand> workingStatusCommands = new ArrayList<>();
            endOfLeaveForms.forEach(leaveFormPO -> {
                SessionContextHolder.requestTenant(leaveFormPO.getTenant());
                var leaveForm = JSONObject.parseObject(leaveFormPO.getLeaveFormDetail(), LeaveForm.class);
                var changeWorkingStatusCommand = ChangeWorkingStatusCommand.builder()
                        .workingStatus(null)
                        .employeeId(leaveForm.getDetail().getUserId())
                        .build();
                workingStatusCommands.add(changeWorkingStatusCommand);
                redisTemplate.delete(waitingOfChangeStaffStatusKey + leaveFormPO.getId());
            });
            staffClient.changeWorkingStatus(workingStatusCommands);
        }
    }
}
