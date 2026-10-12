package com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.ClockCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command.UpdateClockCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.commandgateway.commond.LeavePostCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.dao.LeavePostSituationMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.po.LeavePostSituationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.AttendanceGroupQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.ChangeWorkingStatusCommand;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.WorkingStatus;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LeavePostCommandService {
    private final LeavePostSituationMapper leavePostSituationMapper;
    private final AttendanceGroupQueryService attendanceGroupQueryService;
    private final ClockCommandService clockCommandService;
    private final StaffClient staffClient;

    @Transactional(rollbackFor = Exception.class)
    public void leavePost(LeavePostCommand command) {
        //插入离岗记录
        StaffDetailDTO user = staffClient.getById(command.getApplicantId());
        LeavePostSituationPO lps = ObjectUtils.copying(command, LeavePostSituationPO.class);
        if (user != null) {
            lps.setApplicantName(user.getName());
            lps.setApplicantDeptId(user.getOrganizationId());
            lps.setPostName(user.getAppointmentName());
        }
        //获取考情组
        AttendanceGroupPO myAttendanceGroup = attendanceGroupQueryService.getMyAttendanceGroup(command.getApplicantId());
        lps.setAttendanceGroupId(myAttendanceGroup != null ? myAttendanceGroup.getId() : null);
        leavePostSituationMapper.insert(lps);
    }

    @Transactional(rollbackFor = Exception.class)
    public void leavePostAndUpdateWorkStatus(LeavePostCommand command) {
        //修改工作状态
        //当前时间已在离岗时间范围内，立即跟新状态；其他情况 则通过定时任务进行更新（leavePostForUpdatWorkStatus）
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(command.getStartTime()) && currentTime.isBefore(command.getEndTime())) {
            staffClient.changeWorkingStatus(Collections.singletonList(ChangeWorkingStatusCommand.builder()
                                                                              .employeeId(command.getApplicantId())
                                                                              .workingStatus(WorkingStatus.LEAVE_POST).condition(false)
                                                                              .build()));
            //跟新打卡状态
            clockCommandService.updateClockToNormalByPeriod(UpdateClockCommand.builder()
                                                                    .staffId(command.getApplicantId())
                                                                    .period(Period.of(command.getStartTime(), currentTime))
                                                                    .trigger(ClassesStatusTriggerEnum.LEAVE_THE_POST.name())
                                                                    .build());
        }
        //插入离岗记录
        this.leavePost(command);
    }
}
