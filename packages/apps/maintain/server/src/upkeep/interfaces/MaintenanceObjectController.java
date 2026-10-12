package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenaceObjectService;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lrj
 * @Description 维保对象
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceObjectController {
    private final MaintenaceObjectService maintenaceObjectService;

    /**
     * @Description 健康维保分析
     * @Author lrj
     */
    @GetMapping("/isUpkeepValid")
    public Integer isUpkeepValid(@RequestParam("deviceId") String deviceId, Period period) {
        return maintenaceObjectService.isUpkeepValid(deviceId, period);
    }

}