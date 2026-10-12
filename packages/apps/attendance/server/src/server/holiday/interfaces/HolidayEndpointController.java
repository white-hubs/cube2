package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.init.InitHolidayType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayEndpointController {

    private final InitHolidayType initHolidayType;

    /**
     * 手动初始化假期类型(内部调用)
     */
    @PostMapping("/holiday/endpoint/init")
    public void initHoliday() {
        initHolidayType.execute();
    }
}
