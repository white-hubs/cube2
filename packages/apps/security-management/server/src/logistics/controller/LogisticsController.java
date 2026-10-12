package com.guhecloud.cube.srv.gen.securitymanagement.server.logistics.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.logistics.service.LogisticsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.param.PerimeterStatisticsParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LogisticsController {
    private final LogisticsService logisticsService;

    /**
     * @Description 物流设备状态状态
     * @Author lrj
     */
    @GetMapping("/logistics-area-status-num")
    public Map<String, Object> getLogisticsAreaStatusNum(PerimeterStatisticsParam param) {
        return logisticsService.getLogisticsAreaStatusNum(param);
    }
}
