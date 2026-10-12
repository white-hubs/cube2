package com.guhecloud.cube.base.device.center.open.interfaces;

import com.guhecloud.cube.base.device.center.metadata.param.DeviceGroupBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceGroupParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.DeviceGroupDetailQueryParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.DeviceGroupQueryParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.TagDeviceGroupDeviceQueryParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DeviceGroupPageVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DeviceGroupVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.open.service.DeviceCenterGroupService;
import com.guhecloud.cube.base.device.center.open.service.DeviceCenterService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceGroupController {

    private final DeviceCenterGroupService deviceCenterGroupService;

    private final DeviceCenterService deviceCenterService;

    /**
     * 设备分组关联设备
     *
     * @param cmd 关联参数
     */
    @PostMapping("/device-groups/ref-devices")
    public void addGroupDevice(@Valid @RequestBody DeviceGroupBindDeviceCmd cmd) {
        deviceCenterGroupService.addGroupDevice(cmd);
    }

    /**
     * 标签下的设备分组分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/tags/device-groups")
    public Pagination<DeviceGroupPageVO> deviceGroupPageByTag(@Valid DeviceGroupQueryParam param) {
        return deviceCenterGroupService.deviceGroupPageByTag(param);
    }

    /**
     * 标签下的设备分组列表
     *
     * @param tagCode
     *
     * @return
     */
    @GetMapping("/tags/{tagCode}/device-group-all")
    public List<DeviceGroupVO> deviceGroupsByTag(@PathVariable("tagCode") String tagCode) {
        return deviceCenterGroupService.deviceGroupsByTag(tagCode);
    }

    /**
     * 标签下的设备分组列表
     *
     * @param tagCode
     * @param param
     *
     * @return
     */
    @PostMapping("/tags/{tagCode}/device-group-all/by-device-ids")
    public List<DeviceGroupVO> deviceGroupByTagAndDeviceIds(@PathVariable("tagCode") String tagCode,
                                                            @RequestBody @Valid QueryDeviceGroupParam param
                                                           ) {
        return deviceCenterGroupService.deviceGroupsByTag(tagCode, param);
    }

    /**
     * 标签下设备分组的设备分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/tags/device-group/devices")
    public Pagination<DevicePageInfoVO> getGroupDevicePage(@Valid DeviceGroupDetailQueryParam param) {
        return deviceCenterService.getGroupDevicePage(param);
    }

    /**
     * 标签下设备分组的设备分页列表
     * @param param
     * @return
     */
    @PostMapping("/tags/device-groups-devices/page")
    public Pagination<DevicePageInfoVO> getGroupDevicesPageByTagCode(@Valid @RequestBody TagDeviceGroupDeviceQueryParam param) {
        return deviceCenterService.getGroupDevicesPageByTagCode(param);
    }
}
