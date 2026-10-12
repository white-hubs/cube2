package com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.param.PerimeterStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.service.PerimeterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PerimeterStatisticsController {
    private final PerimeterService perimeterService;

    @GetMapping("/area-offline-num")
    public Map<String, Object> getAreaOfflineNum(PerimeterStatisticsParam param) {
        return perimeterService.getAreaOfflineNum(param);
    }

}
