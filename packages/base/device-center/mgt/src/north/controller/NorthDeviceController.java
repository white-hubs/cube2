package com.guhecloud.cube.iot.mgt.north.controller;

import com.guhecloud.cube.iot.mgt.device.param.DeviceAddParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceCreateParam;
import com.guhecloud.cube.iot.mgt.device.service.DevDeviceService;
import com.guhecloud.cube.iot.mgt.north.param.DeviceDetailParam;
import com.guhecloud.cube.iot.mgt.north.param.DeviceQueryParam;
import com.guhecloud.cube.iot.mgt.north.server.impl.NorthDeviceService;
import com.guhecloud.cube.iot.mgt.north.vo.DeviceDetail;
import com.guhecloud.cube.iot.mgt.north.vo.DeviceThin;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/north/device")
public class NorthDeviceController {
    @Resource
    private NorthDeviceService northDeviceService;
    @Resource
    private DevDeviceService devDeviceService;

    /**
     * 设备分页查询
     *
     * @param deviceQueryParam 查询参数
     *
     * @return 分页结果
     */
    @GetMapping("/page")
    public Pagination<DeviceThin> page(@SpringQueryMap DeviceQueryParam deviceQueryParam) {
        return northDeviceService.page(deviceQueryParam);
    }

    /**
     * 设备详情
     *
     * @param deviceDetailParam deviceDetailParam
     *
     * @return DeviceDetail
     */
    @GetMapping("/detail")
    public DeviceDetail detail(@SpringQueryMap DeviceDetailParam deviceDetailParam) {
        return northDeviceService.detail(deviceDetailParam);
    }

    /**
     * 返回设备下的属性名称
     *
     * @param deviceId 设备ID
     *
     * @return map
     */
    @GetMapping("/devicePropName")
    public Map<String, String> devicePropName(@RequestParam("deviceId") String deviceId) {
        return northDeviceService.devicePropName(deviceId);
    }

    /**
     * 根据原始设备ID查询物联平台中的设备ID<br/>
     * 此方法非标准北向接口，目前仅理货机代理使用！！！！
     *
     * @param rawDeviceId 原始设备ID
     *
     * @return 设备ID列表
     */
    @GetMapping("/deviceIds/by-raw-id/{rawDeviceId}")
    public List<String> listDeviceIdByRawDeviceId(@PathVariable("rawDeviceId") String rawDeviceId) {
        return northDeviceService.listDeviceIdByRawDeviceId(rawDeviceId);
    }

    /**
     * 添加设备
     *
     * @param deviceAddParam 设备信息
     *
     * @return 设备id
     */
    @PostMapping("/add")
    public Long addDevice(@RequestBody DeviceAddParam deviceAddParam) {
        DeviceCreateParam deviceCreateParam = new DeviceCreateParam();
        deviceCreateParam.setProductId(deviceAddParam.getProductId());
        deviceCreateParam.setDeviceNumber(deviceAddParam.getDeviceNumber());
        deviceCreateParam.setDeviceName(
                StringUtils.isBlank(deviceAddParam.getDeviceName()) ? deviceAddParam.getDeviceNumber() : deviceAddParam.getDeviceName());
        deviceCreateParam.setRemark(deviceAddParam.getRemark());
        return devDeviceService.addDevice(deviceCreateParam);
    }
}
