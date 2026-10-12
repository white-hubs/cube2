package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttClockInDetailsApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInDetailsParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-18  14:32
 * @Description: 人事组件管理
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClockInDetailsController {
    private final AttClockInDetailsApplication clockInDetailsApplication;

    /**
     * 获取人员出勤情况
     */
    @GetMapping("/personnel/getClockInDetailsByUserId")
    public String getClockInDetailsByUserId(AttClockInDetailsParam param) {
        return clockInDetailsApplication.getClockInDetailsByUserId(param);
    }
}
