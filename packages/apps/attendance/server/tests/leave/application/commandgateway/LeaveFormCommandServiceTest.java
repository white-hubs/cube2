package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.AttendanceManagementServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.EmployeeHolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.EmployeeHoliday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.LeaveApprovedCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.LeaveRefuseCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.SubmitLeaveFormCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.WithholdCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.querygateway.LeaveFormQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@Slf4j
@SpringBootTest(classes = AttendanceManagementServer.class)
@RunWith(SpringRunner.class)
public class LeaveFormCommandServiceTest {
    @Autowired
    private LeaveFormCommandService leaveFormCommandService;
    @Autowired
    private LeaveFormQueryService leaveFormQueryService;
    @Autowired
    private EmployeeHolidayQueryService employeeHolidayQueryService;

    @Before
    public void before() {
        SessionContextHolder.requestTenant(9578171);
        SecurityContextHolder.setPrincipal(Principal.builder().account("1").id(1L).type(Principal.Type.CLIENT).build());
    }

    @Test
    public void withhold() {
        // 查询假期余额
        EmployeeHoliday beforeBalance = employeeHolidayQueryService.getById(2L);
        leaveFormCommandService.withhold(new WithholdCommand(1597548856230092802L));
        EmployeeHoliday afterBalance = employeeHolidayQueryService.getById(2L);
    }

    @Test
    public void submit() {
        // 查询请假单

        // 提交请假单
        SubmitLeaveFormCommand submitLeaveFormCommand = SubmitLeaveFormCommand.builder()
                .name("请假单")
                .leaveTime(
                        new LeaveFormDetail.LeaveTime("2023-01-01 09:00:00,2023-01-01 18:00:00",
                                                      new LeaveFormDetail.LeaveTime.Time(), new LeaveFormDetail.LeaveTime.Time())
                          )
                .leaveType(Holiday.HolidayType.ANNUAL_LEAVE)
                .submitTime(new Date())
                .submitUserId(2L)
                .build();
        Long submitId = leaveFormCommandService.submit(submitLeaveFormCommand);

        // 查询请假单
        LeaveForm leaveForm = leaveFormQueryService.getById(submitId);

        Asserts.check(leaveForm.getId().equals(submitId), "提交请假单失败");
    }

    @Test
    public void approved() {
        LeaveApprovedCommand leaveApprovedCommand = new LeaveApprovedCommand(1597548404323127298L);
        leaveFormCommandService.approved(leaveApprovedCommand);
        EmployeeHoliday afterBalance = employeeHolidayQueryService.getById(2L);
    }

    @Test
    public void refuse() {
        EmployeeHoliday beforeBalance = employeeHolidayQueryService.getById(2L);
        LeaveRefuseCommand leaveRefuseCommand = new LeaveRefuseCommand(1597548856230092802L);
        leaveFormCommandService.refuse(leaveRefuseCommand);
        EmployeeHoliday afterBalance = employeeHolidayQueryService.getById(2L);
        log.info("余额:{}", afterBalance.getHolidayBalances());
    }

    @Test
    public void preSubmit() {

    }
}
