package com.guhecloud.cube.base.device.center.open.interfaces;

import com.guhecloud.cube.base.device.center.metadata.annotation.InvokeForm;
import com.guhecloud.cube.base.device.center.metadata.std.vo.AssociatedVideoVO;
import com.guhecloud.cube.base.device.center.open.service.DeviceCenterAssociatedService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

/**
 * 设备关联接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterAssociatedController {

    private final DeviceCenterAssociatedService deviceCenterAssociatedService;

    /**
     * 获取设备关联的监控设备列表
     *
     * @param deviceInstanceId 设备实例id(IotDeviceId==DeviceId)
     *
     * @return 监控设备列表
     */
    @GetMapping("/device/instance/{deviceInstanceId}/associated/videos")
    public List<AssociatedVideoVO> getAssociatedVideosByDeviceId(@PathVariable("deviceInstanceId") Long deviceInstanceId) {
        return deviceCenterAssociatedService.getAssociatedVideosByDeviceId(deviceInstanceId);
    }

    /**
     * 获取所有设备关联的监控设备列表
     *
     * @return 监控设备列表
     */
    @GetMapping("/device/instance/associated/videos")
    public Pagination<AssociatedVideoVO> getAssociatedVideos(Paging paging) {
        return deviceCenterAssociatedService.getAssociatedVideos(paging);
    }

    /**
     * 过滤掉非视频设备
     *
     * @param deviceIds 设备id列表
     *
     * @return 设备id列表
     */
    @InvokeForm
    @PostMapping("/device/instance/associated/videos/filter")
    public Set<Long> filterNotVideoDeviceIds(@RequestBody Set<Long> deviceIds) {
        return deviceCenterAssociatedService.filterNotVideoDeviceIds(deviceIds);
    }

    /**
     * 获取设备关联的设备id集合
     *
     * @param deviceId
     *
     * @return
     */
    @GetMapping("/device/instance/associated/ids")
    public Set<Long> getAssociatedDeviceIds(@RequestParam @NotNull(message = "参数错误，设备不能为空") Long deviceId) {
        return deviceCenterAssociatedService.getAssociatedDeviceIds(deviceId);
    }
}
