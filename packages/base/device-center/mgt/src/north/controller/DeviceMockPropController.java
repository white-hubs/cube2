package com.guhecloud.cube.iot.mgt.north.controller;

import com.guhecloud.cube.iot.mgt.core.data.msg.device.MockDevEventReport;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.MockPropReport;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.def.cache.DeviceCacheSupport;
import com.guhecloud.cube.iot.mgt.core.innerbus.SouthInnerBus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 提供设备模拟属性上报、事件上报接口
 */
@Slf4j
@RestController
@RequestMapping("/public/device/mock")
public class DeviceMockPropController {

    @Resource
    private SouthInnerBus southInnerBus;

    @Resource
    private DeviceCacheSupport deviceCacheSupport;

    /**
     * 模拟设备上报属性
     *
     * @param mockPropReport 设备属性上报消息
     *
     * @return 是否成功
     */
    @RequestMapping("/prop")
    public Boolean mockProp(@RequestBody MockPropReport mockPropReport) {

        Optional<DeviceDigestVo> deviceDigestVo = deviceCacheSupport.queryDeviceDigestCache(mockPropReport.getDeviceId());
        if (deviceDigestVo.isEmpty()) {
            log.warn("设备不存在，无法模拟属性上报，deviceId:{}", mockPropReport.getDeviceId());
            return false;
        }

        // 补齐设备信息
        DeviceDigestVo deviceDigest = deviceDigestVo.get();
        mockPropReport.setDeviceNumber(deviceDigest.getDeviceNumber());
        mockPropReport.setParentDeviceId(deviceDigest.getParentDeviceId());
        mockPropReport.setProductId(deviceDigest.getProdId());
        mockPropReport.setTenantId(deviceDigest.getTenantId());

        southInnerBus.process(mockPropReport);
        return true;
    }

    /**
     * 模拟设备上报事件
     *
     * @param mockDevEventReport 设备事件消息
     *
     * @return 是否成功
     */
    @RequestMapping("/event")
    public Boolean mockEvent(@RequestBody MockDevEventReport mockDevEventReport) {
        Optional<DeviceDigestVo> deviceDigestVo = deviceCacheSupport.queryDeviceDigestCache(mockDevEventReport.getDeviceId());
        if (deviceDigestVo.isEmpty()) {
            log.warn("设备不存在，无法模拟事件上报，deviceId:{}", mockDevEventReport.getDeviceId());
            return false;
        }

        // 补齐设备信息
        DeviceDigestVo deviceDigest = deviceDigestVo.get();
        mockDevEventReport.setProductId(deviceDigest.getProdId());
        mockDevEventReport.setTenantId(deviceDigest.getTenantId());

        southInnerBus.process(mockDevEventReport);
        return true;
    }

}
