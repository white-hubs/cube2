package com.guhecloud.cube.base.device.center.open.interfaces.client;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoWithDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.open.service.DeviceCenterService;
import com.guhecloud.cube.base.device.center.open.service.DeviceInstanceService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 设备中间client暴露接口
 *
 * @author lanchun
 * @date 2024/1/11 13:50
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterClientAdapter {

    private final DeviceCenterService deviceCenterService;
    private final DeviceInstanceService deviceInstanceService;

    /**
     * 根据设备id集合查询设备基础信息列表
     *
     * @param deviceIds 设备id
     *
     * @return 设备基础档案信息
     */
    @GetMapping("/device/base/information/list")
    public List<DeviceBaseArchivesInfoDTO> getListByDeviceId(@RequestParam Set<Long> deviceIds) {
        return deviceCenterService.getListByDeviceId(deviceIds);
    }

    /**
     * 根据设备id集合查询设备基础信息列表
     *
     * @param param 设备id
     *
     * @return 设备基础档案信息
     */
    @PostMapping("/device/base/information/list/post")
    public List<DeviceBaseArchivesInfoDTO> postListByDeviceId(@RequestBody DevicePostByIdSetParam param) {
        return deviceCenterService.getListByDeviceId(param.getDeviceIdSet());
    }

    /**
     * 根据设备id集合查询设备扩展信息列表
     *
     * @param deviceIds 设备id
     *
     * @return 设备扩展信息
     */
    @GetMapping("/device/expand/information/list")
    List<DeviceExpandInfoDTO> getExpandListByDeviceId(@RequestParam Set<Long> deviceIds) {
        return deviceCenterService.getExpandListByDeviceId(deviceIds);
    }

    @GetMapping("/device/product/information/list")
    public List<DeviceProductInfoWithDeviceDTO> getProductInfoByDeviceId(@RequestParam Set<Long> deviceIds) {
        return deviceCenterService.getProductInfoByDeviceId(deviceIds);
    }

    @PostMapping("/device/product/information/list/post")
    public List<DeviceProductInfoWithDeviceDTO> postProductInfoByDeviceId(@RequestBody DevicePostByIdSetParam param) {
        return deviceCenterService.getProductInfoByDeviceId(param.getDeviceIdSet());
    }

    /**
     * 根据设备id查询设备详细信息  不允许循环调用
     *
     * @param deviceId 设备id
     *
     * @return 设备详情
     */
    @GetMapping("/device/detail/{deviceId}")
    public DeviceDetailDTO getDeviceById(@PathVariable("deviceId") Long deviceId) {
        return deviceInstanceService.getById(deviceId);
    }

    /**
     * 根据标签集合查询设备列表
     * <p>
     * tagTypeCode
     *
     * @param tagCodes 设备标签
     *
     * @return
     */
    @GetMapping("/device/tag/base/information/list")
    public List<DeviceBaseArchivesInfoDTO> getListByTag(@RequestParam Set<String> tagCodes) {
        return deviceCenterService.getListByTag(tagCodes);
    }

    /**
     * 根据设备管理员获取设备实例
     *
     * @param managerId 管理人员id
     *
     * @return 设备id集合
     */
    @GetMapping("/device/manager/{managerId}/list")
    public List<DeviceBaseArchivesInfoDTO> listByManager(
            @PathVariable("managerId") @NotNull(message = "参数错误，设备管理员不能为空") Long managerId
                                                        ) {
        return deviceCenterService.getListByManager(managerId);
    }

    /**
     * 获取指定设备关联的监控设备信息
     *
     * @param deviceId 设备id
     *
     * @return
     */
    @GetMapping("/device/associated/videos")
    public Set<DeviceBaseArchivesInfoDTO> getAssociatedVideos(@RequestParam @NotNull(message = "参数错误，设备不能为空") Long deviceId) {
        return deviceCenterService.getAssociatedVideos(deviceId);
    }

    /**
     * 批量查询设备基础信息
     *
     * @param deviceIds 设备id
     *
     * @return 设备信息
     */
    @GetMapping("/device/details/list")
    public List<DeviceBaseDetailsDTO> getDetailsListByDeviceId(@RequestParam(value = "deviceIds") Set<Long> deviceIds) {
        return deviceCenterService.getDetailsListByDeviceId(deviceIds);
    }

    /**
     * 按三方设备 id（center_biz_device.thirdparty_id）查一条设备基础详情。
     */
    @GetMapping("/device/details/by-third-party-id")
    public DeviceBaseDetailsDTO getDetailsByThirdPartyId(@RequestParam("thirdPartyId") String thirdPartyId) {
        return deviceCenterService.getDetailsByThirdPartyId(thirdPartyId);
    }

    /**
     * 批量查询设备基础信息
     *
     * @param param deviceIdSet 设备id
     *
     * @return 设备信息
     */
    @PostMapping("/device/details/post/list")
    public List<DeviceBaseDetailsDTO> postDetailsListByDeviceId(@RequestBody DevicePostByIdSetParam param) {
        return deviceCenterService.getDetailsListByDeviceId(param.getDeviceIdSet());
    }

}
