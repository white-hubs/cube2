package com.guhecloud.cube.base.device.center.core.controller;

import com.guhecloud.cube.base.device.center.core.dto.DeviceRefInfoDTO;
import com.guhecloud.cube.base.device.center.core.param.CreateDeviceRefParam;
import com.guhecloud.cube.base.device.center.core.service.CenterDeviceRefService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 此关联仅针对视频设备<br/>
 * 其他设备关联请重写代码实现！！！！
 */
@Slf4j
@RestController
public class CenterDeviceRefController {
    @Resource
    private CenterDeviceRefService centerDeviceRefService;

    /**
     * 查询设备关联信息
     *
     * @param deviceId 设备ID
     *
     * @return 关联信息
     */
    @GetMapping("/device-ref/detail/{deviceId}")
    public DeviceRefInfoDTO findDeviceRefInfo(@PathVariable Long deviceId) {
        return centerDeviceRefService.findDeviceRefInfo(deviceId);
    }

    /**
     * 关联设备
     *
     * @param createDeviceRefParam 参数
     *
     * @return 是否成功
     */
    @PostMapping("/device-ref/ref/{deviceId}")
    public Boolean refDevice(@PathVariable Long deviceId, @RequestBody CreateDeviceRefParam createDeviceRefParam) {
        return centerDeviceRefService.refDevice(deviceId, createDeviceRefParam);
    }
}
