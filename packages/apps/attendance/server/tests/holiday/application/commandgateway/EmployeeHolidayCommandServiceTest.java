package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.AttendanceManagementServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.DeductHolidayBalanceCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.ExpireCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.GrantHolidayCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.EmployeeHolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.EmployeeHoliday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayBalance;
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

import java.util.stream.Collectors;

@Slf4j
@SpringBootTest(classes = AttendanceManagementServer.class)
@RunWith(SpringRunner.class)
public class EmployeeHolidayCommandServiceTest {

    @Autowired
    private EmployeeHolidayCommandService employeeHolidayCommandService;
    @Autowired
    private EmployeeHolidayQueryService employeeHolidayQueryService;

    @Before
    public void before() {
        SessionContextHolder.requestTenant(58197260);
        SecurityContextHolder.setPrincipal(Principal.builder().account("1").id(1L).type(Principal.Type.CLIENT).build());
    }

    /**
     * 发放假期单元测试
     */
    @Test
    public void grantHoliday() {
        // 查询员工余额
        EmployeeHoliday beforeEmployeeHoliday = employeeHolidayQueryService.getById(2L);
        // 发放假期
        GrantHolidayCommand grantHolidayCommand = new GrantHolidayCommand(2L, 100.0f, Holiday.HolidayType.ANNUAL_LEAVE);
        employeeHolidayCommandService.grantHoliday(grantHolidayCommand);
        // 查询员工余额
        EmployeeHoliday afterEmployeeHoliday = employeeHolidayQueryService.getById(2L);

        HolidayBalance COMPENSATORY_LEAVE = afterEmployeeHoliday.getHolidayBalances().stream()
                .filter(holidayBalance -> holidayBalance.getHolidayType() == Holiday.HolidayType.COMPENSATORY_LEAVE)
                .collect(Collectors.toList()).get(0);

        Asserts.check(COMPENSATORY_LEAVE.getRemainingBalance() >= 0, "增加剩余余额失败");
    }

    @Test
    public void expire() {
        // 查询员工余额
        EmployeeHoliday beforeEmployeeHoliday = employeeHolidayQueryService.getById(1L);
        log.info("失效前员工剩余余额:{}", beforeEmployeeHoliday.getHolidayBalances()
                .stream().filter(holidayBalance -> holidayBalance.getHolidayType() == Holiday.HolidayType.COMPENSATORY_LEAVE)
                .collect(Collectors.toList()).get(0).getRemainingBalance());

        ExpireCommand expireCommand = new ExpireCommand(Holiday.HolidayType.COMPENSATORY_LEAVE, 1L, 1.0f);
        employeeHolidayCommandService.expire(expireCommand);

        // 查询员工余额
        EmployeeHoliday afterEmployeeHoliday = employeeHolidayQueryService.getById(1L);
        log.info("失效后员工剩余余额:{}", afterEmployeeHoliday.getHolidayBalances()
                .stream().filter(holidayBalance -> holidayBalance.getHolidayType() == Holiday.HolidayType.COMPENSATORY_LEAVE)
                .collect(Collectors.toList()).get(0).getRemainingBalance());
    }

    @Test
    public void deduct() {
        DeductHolidayBalanceCommand deductHolidayBalanceCommand = new DeductHolidayBalanceCommand(1527245265659052033L, 1.0f, "", Holiday.HolidayType.COMPENSATORY_LEAVE);
        employeeHolidayCommandService.deduct(deductHolidayBalanceCommand);
    }
}
