package com.guhecloud.cube.base.device.center.std.devcenter;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

@FeignClient(CLIENT_NAME)
public interface DeviceCenterAssociatedStdClient {

    /**
     * 获取指定设备关联的监控设备信息
     *
     * @param deviceId 设备id
     *
     * @return
     */
    @GetMapping("/device/associated/videos")
    Set<DeviceBaseArchivesInfoDTO> getAssociatedVideos(
            @RequestParam(value = "deviceId") @NotNull(message = "参数错误，设备不能为空") Long deviceId
                                                      );

    /**
     * 获取设备关联的设备id集合
     *
     * @param deviceId
     *
     * @return
     */
    @GetMapping("/device/instance/associated/ids")
    Set<Long> getAssociatedDeviceIds(@RequestParam @NotNull(message = "参数错误，设备不能为空") Long deviceId);

    /**
     * 获取三维空间关联的摄像机列表
     *
     * @param deviceId
     *
     * @return
     */
    @GetMapping("/device/spatial-coordinates/associated/videos")
    Set<DeviceBaseArchivesInfoDTO> getAssociatedSpatialCoordinatesVideos(
            @RequestParam @NotNull(message = "参数错误，设备不能为空") Long deviceId
                                                                        );

    /**
     * 获取三维空间关联的设备id集合
     *
     * @param deviceId
     *
     * @return
     */
    @GetMapping("/device/spatial-coordinates/associated/ids")
    Set<Long> getAssociatedSpatialCoordinatesDevices(@RequestParam @NotNull(message = "参数错误，设备不能为空") Long deviceId);
}