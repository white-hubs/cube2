package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper;

import com.guhecloud.cube.srv.gen.attendance.management.AttendanceManagementServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.ExpiryDate;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.job.ClearUpExpiryHolidayTask;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = AttendanceManagementServer.class)
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class HolidayGrantMapperTest {
    @Autowired
    private HolidayGrantMapper holidayGrantMapper;

    @Test
    public void selectExpiringListIgnoreTenant() {
        ClearUpExpiryHolidayTask.CacheValue cacheValue1 = new ClearUpExpiryHolidayTask.CacheValue(
                ExpiryDate.builder().expiryDateType(ExpiryDate.ExpiryDateType.CALENDAR_YEAR).value(1).build(),
                1L,
                Holiday.HolidayType.COMPENSATORY_LEAVE
        );

        ClearUpExpiryHolidayTask.CacheValue cacheValue2 = new ClearUpExpiryHolidayTask.CacheValue(
                ExpiryDate.builder().expiryDateType(ExpiryDate.ExpiryDateType.FULL).timeUnit(ExpiryDate.TimeUnit.MONTH).value(1).build(),
                1L,
                Holiday.HolidayType.COMPENSATORY_LEAVE
        );

        holidayGrantMapper.selectExpiringListIgnoreTenant(
                Sets.newLinkedHashSet(cacheValue1, cacheValue2)
                                                         );
    }
}
