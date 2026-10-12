package com.guhecloud.cube.iot.north.bridge.client;

import com.guhecloud.cube.base.device.center.metadata.param.ChangeDeviceLntLatCmd;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceThinDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceAddParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static com.guhecloud.cube.iot.north.bridge.client.Profile.CLIENT_NAME;

@FeignClient(name = CLIENT_NAME)
public interface IotDeviceClient {

    /**
     * 分页检索设备信息
     *
     * @param deviceQueryParam deviceQueryParam
     *
     * @return page
     */
    @GetMapping("/north/device/page")
    Pagination<DeviceThinDTO> page(@SpringQueryMap DeviceQueryParam deviceQueryParam);

    /**
     * 设备详情
     *
     * @param deviceDetailParam deviceDetailParam
     *
     * @return DeviceDetail
     */
    @GetMapping("/north/device/detail")
    DeviceDetailDTO detail(@SpringQueryMap DeviceDetailParam deviceDetailParam);

    /**
     * 根据设备ID查询设备属性名称
     *
     * @param deviceId 设备ID
     *
     * @return PairDTO < String, String>
     */
    @GetMapping("/north/device/devicePropName")
    Map<String, String> devicePropName(@RequestParam("deviceId") String deviceId);

    /**
     * 添加设备
     *
     * @param deviceAddParam 设备信息
     *
     * @return 设备id
     */
    @PostMapping("/north/device/add")
    Long addDevice(@RequestBody DeviceAddParam deviceAddParam);

    /**
     * 更新设备经纬度与/或位置详细描述（设备中心扩展表 lnt_and_lat、location_des）
     *
     * @param cmd 设备 ID；可选 lntAndLat（一般为「经度,纬度」）、locationDes；二者至少传其一
     *
     * @return 是否成功
     */
    @PutMapping("/device-instance/device/lnt-and-lat")
    Boolean changeDeviceLntLat(@RequestBody @Valid ChangeDeviceLntLatCmd cmd);
}
