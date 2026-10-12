package com.guhecloud.cube.base.device.center.std.devcenter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

@FeignClient(CLIENT_NAME)
public interface DeviceBimSceneStdClient {

    /**
     * 设备主键列表查询(根据场景获取)
     *
     * @param sceneType
     * @param areaId
     *
     * @return
     */
    @GetMapping("/device-bim-scene/device-ids")
    Set<Long> getBimDeviceIds(@RequestParam(value = "sceneType") Long sceneType,
                              @RequestParam(required = false, value = "areaId") Long areaId
                             );
    /**
     * 设备code列表查询(根据场景获取)
     *
     * @param sceneType
     * @param areaId
     *
     * @return
     */
    @GetMapping("/device-bim-scene/device-codes")
    Set<String> getBimDeviceCodes(@RequestParam(value = "sceneType") Long sceneType,
                                         @RequestParam(required = false, value = "areaId") Long areaId
                                        );
}
