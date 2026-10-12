package com.guhecloud.cube.srv.gen.securitymanagement.server.threedimensional.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.threedimensional.param.DeviceStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.threedimensional.service.DeviceStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description 三维可视化
 * @Author lrj
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThreeDimensionalController {

    private final DeviceStatisticsService deviceStatisticsService;

    /**
     * @Description 设备报警率
     */
    @GetMapping("/equipment-alarm-rate")
    public Map<String, Object> getEquipmentAlarmRate(DeviceStatisticsParam param) {

        return deviceStatisticsService.getEquipmentAlarmRate(param);
    }

}
