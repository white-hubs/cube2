package com.guhecloud.cube.base.device.center.open.interfaces;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.open.service.DeviceSpatialCoordinatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceSpatialCoordinatesController {

    private final DeviceSpatialCoordinatesService deviceSpatialCoordinatesService;

    /**
     * 获取三维空间关联的摄像机列表
     *
     * @param deviceId
     *
     * @return
     */
    @GetMapping("/device/spatial-coordinates/associated/videos")
    public Set<DeviceBaseArchivesInfoDTO> getAssociatedSpatialCoordinatesVideos(
            @RequestParam @NotNull(message = "参数错误，设备不能为空") Long deviceId
                                                                               ) {
        return deviceSpatialCoordinatesService.getAssociatedSpatialCoordinatesVideos(deviceId);
    }

    /**
     * 获取三维空间关联的设备id集合
     *
     * @param deviceId
     *
     * @return
     */
    @GetMapping("/device/spatial-coordinates/associated/ids")
    public Set<Long> getAssociatedSpatialCoordinatesDevices(@RequestParam @NotNull(message = "参数错误，设备不能为空") Long deviceId) {
        return deviceSpatialCoordinatesService.getRangeDevices(deviceId);
    }
}
