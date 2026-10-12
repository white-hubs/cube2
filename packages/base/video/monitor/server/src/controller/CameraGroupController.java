package com.guhecloud.cube.biz.vmonitor.server.controller;

import com.guhecloud.cube.base.device.center.metadata.vo.DeviceGroupVO;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.auth.service.CameraAuthService;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service.CameraSecurityDeviceQueryService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class CameraGroupController {

    private final CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService;

    private final CameraAuthService cameraAuthService;

    public CameraGroupController(CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService, CameraAuthService cameraAuthService
                                ) {
        this.cameraSecurityDeviceQueryService = cameraSecurityDeviceQueryService;
        this.cameraAuthService = cameraAuthService;
    }

    /**
     * 获取监控分组列表
     *
     * @return
     */
    @GetMapping("/camera/groups")
    public List<DeviceGroupVO> deviceGroupByTagAndDeviceIds() {
        var deviceIds = cameraAuthService.getUserAuthScopeDeviceIds(SecurityContextHolder.getPrincipal().getId());
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }
        return cameraSecurityDeviceQueryService.getDeviceGroups(deviceIds);
    }
}
