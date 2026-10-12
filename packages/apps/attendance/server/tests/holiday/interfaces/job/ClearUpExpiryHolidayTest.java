package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.job;

import com.guhecloud.cube.srv.gen.attendance.management.AttendanceManagementServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(classes = AttendanceManagementServer.class)
@RunWith(SpringRunner.class)
public class ClearUpExpiryHolidayTest {
    @Autowired
    private ClearUpExpiryHolidayTask clearUpExpiryHolidayTask;

    @Test
    public void execute() {
        clearUpExpiryHolidayTask.execute("");
    }
}
