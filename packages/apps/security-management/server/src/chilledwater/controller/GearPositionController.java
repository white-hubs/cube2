package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.job.WholesalersDataJob;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.DeviceAssociationPositionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.DevicePositionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.GearPositionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DevicePositionVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GearPositionController {
    private final GearPositionService gearPositionService;

    private final WholesalersDataJob wholesalersDataJob;

    /**
     * @Description 设备配置列表
     * @Author lrj
     */
    @GetMapping("/device-position")
    public Pagination<DevicePositionVo> devicePositions(DevicePositionParam pram) {
        return gearPositionService.devicePositions(pram);
    }

    /**
     * @Description 设备配置关联
     * @Author lrj
     */
    @PostMapping("/device-position-association")
    public void deviceAssociationAssociationPositions(@RequestBody List<DeviceAssociationPositionParam> prams) {
        gearPositionService.deviceAssociationAssociationPositions(prams);
    }

    /**
     * @Description 设备配置取消关联
     * @Author lrj
     */
    @GetMapping("/cancellation-device")
    public void cancellationDevice(@RequestParam("gearPositionId") Long gearPositionId, @RequestParam("deviceId") Long deviceId) {
        gearPositionService.cancellationDevice(gearPositionId, deviceId);
    }

    /**
     * @Description 关联档位ids
     * @Author lrj
     */
    @GetMapping("/cold-gear-position-id")
    public List<Long> getGearPositionIds() {
        List<Long> gearPositionIds = gearPositionService.getGearPositionIds();
        return gearPositionIds;
    }

    @GetMapping("/aaaaa")
    public void add() {
        wholesalersDataJob.wholesalersData("58197260");
    }

}
