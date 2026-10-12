package com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.param.TrafficLightParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.service.TrafficLightService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.vo.TrafficLightVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrafficLightController {

    private final TrafficLightService trafficLightService;

    /**
     * @Description 交通灯类
     * @Author lrj
     */
    @GetMapping("/traffic-light-page")
    public Pagination<TrafficLightVo> getTrafficLightPage(TrafficLightParam param) {
        return trafficLightService.getTrafficLightPage(param);
    }
}
