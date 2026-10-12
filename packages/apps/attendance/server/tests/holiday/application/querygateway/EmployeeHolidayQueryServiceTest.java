package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Sets;
import com.guhecloud.cube.srv.gen.attendance.management.AttendanceManagementServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayConsumptionDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.EmployeeHoliday;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.apache.http.util.Asserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest(classes = AttendanceManagementServer.class)
@RunWith(SpringRunner.class)
public class EmployeeHolidayQueryServiceTest {
    @Autowired
    private EmployeeHolidayQueryService employeeHolidayQueryService;

    @Before
    public void before() {
        SessionContextHolder.requestTenant(9578171);
    }

    /**
     * 假期余额查询
     */
    @Test
    public void getById() {
        EmployeeHoliday employeeHoliday = employeeHolidayQueryService.getById(1L);
        log.info("假期余额查询");
    }

    @Test
    public void getConsumptionSummaryByStaff() {
        List<HolidayConsumptionDTO> consumptionSummaryByStaff = employeeHolidayQueryService.getConsumptionSummaryByStaff(
                Sets.newHashSet(2L),
                new Period(DateUtil.parse("2022-12-19 00:00:00"), new Date())
                                                                                                                        );

        Asserts.check(CollectionUtils.isNotEmpty(consumptionSummaryByStaff), "查询假期消费失败");
    }
}
