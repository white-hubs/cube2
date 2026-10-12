package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway;

import cn.hutool.core.date.DateUtil;
import com.guhecloud.cube.srv.gen.attendance.management.AttendanceManagementServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.tool.StatutoryHolidayResponse;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.apache.http.util.Asserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(classes = AttendanceManagementServer.class)
@RunWith(SpringRunner.class)
public class HolidayQueryServiceTest {
    @Autowired
    private HolidayQueryService holidayQueryService;

    @Test
    public void isStatutoryHoliday() {
        boolean noExist = holidayQueryService.isStatutoryHoliday(new Date());
        Asserts.check(!noExist, "节假日不存在判断错误");

        boolean exist = holidayQueryService.isStatutoryHoliday(
                DateUtil.parse("2022-10-01", "yyyy-MM-dd")
                                                              );
        Asserts.check(exist, "节假日存在判断错误");
    }

    @Test
    public void getStatutoryHolidaysByPeriod() {
        List<StatutoryHolidayResponse> statutoryHolidaysByPeriod = holidayQueryService.getStatutoryHolidaysByPeriod(new Period(
                DateUtil.parse("2022-10-01", "yyyy-MM-dd"),
                DateUtil.parse("2022-11-01", "yyyy-MM-dd")
        ));

        Asserts.check(CollectionUtils.isNotEmpty(statutoryHolidaysByPeriod), "判断错误");
    }
}
