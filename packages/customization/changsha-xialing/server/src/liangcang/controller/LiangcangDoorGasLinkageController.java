package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.service.DoorGasLinkageConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigDetailResponse;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigPageRequest;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigPageResponse;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigSaveRequest;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 门禁气体联动配置。 */
@RestController
@RequiredArgsConstructor
public class LiangcangDoorGasLinkageController {

    private final DoorGasLinkageConfigService doorGasLinkageConfigService;

    /** 分页查询门禁气体联动配置。 */
    @GetMapping("/liangcang/door-gas-linkage/config/page")
    public Pagination<DoorGasLinkageConfigPageResponse> page(
            @ModelAttribute DoorGasLinkageConfigPageRequest request) {
        return doorGasLinkageConfigService.page(request);
    }

    /** 查询指定门禁设备的气体联动配置详情。 */
    @GetMapping("/liangcang/door-gas-linkage/doors/{doorDeviceId}/config")
    public DoorGasLinkageConfigDetailResponse detail(@PathVariable("doorDeviceId") Long doorDeviceId) {
        return doorGasLinkageConfigService.detail(doorDeviceId);
    }

    /** 保存指定门禁设备的气体联动配置。 */
    @PutMapping("/liangcang/door-gas-linkage/doors/{doorDeviceId}/config")
    public Boolean saveConfig(@PathVariable("doorDeviceId") Long doorDeviceId,
                              @RequestBody DoorGasLinkageConfigSaveRequest request) {
        return doorGasLinkageConfigService.saveConfig(doorDeviceId, request);
    }
}
