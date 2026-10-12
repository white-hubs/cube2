package com.guhecloud.cube.customization.xialing.server.communal.controller;

import com.guhecloud.cube.customization.xialing.server.communal.service.XiaNingDeviceStatService;
import com.guhecloud.cube.customization.xialing.server.communal.vo.DeviceStatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备统计控制器
 */
@RestController
@RequiredArgsConstructor
public class XiaNingDeviceStatController {
    private final XiaNingDeviceStatService xiaNingDeviceStatService;

    /**
     * 设备在/离线统计
     *
     * @return 设备统计信息
     */
    @GetMapping("/xianing/device/count-stat")
    public DeviceStatisticsVO statistics() {
        return xiaNingDeviceStatService.statistics();
    }
}
