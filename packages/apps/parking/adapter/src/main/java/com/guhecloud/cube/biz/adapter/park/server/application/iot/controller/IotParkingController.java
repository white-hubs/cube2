package com.guhecloud.cube.biz.adapter.park.server.application.iot.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.iot.service.IotParkingService;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * iot停车场接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IotParkingController {

    private final IotParkingService iotParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/iot/parking-info")
    public List<ParkingInfoVO> getParkingInfo() {
        return this.iotParkingService.getParkingInfo();
    }
}
