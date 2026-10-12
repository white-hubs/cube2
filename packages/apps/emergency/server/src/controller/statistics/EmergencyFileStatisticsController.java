package com.guhecloud.cube.biz.emergency.server.controller.statistics;

import com.guhecloud.cube.biz.emergency.server.param.statistics.EmergencyFileStatisticsParam;
import com.guhecloud.cube.biz.emergency.server.service.statistics.EmergencyFileStatisticsService;
import com.guhecloud.cube.biz.emergency.server.vo.statistics.EmergencyFileStatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileStatisticsController {

    private final EmergencyFileStatisticsService emergencyFileStatisticsService;

    @GetMapping("/emergency-file/statistics")
    public EmergencyFileStatisticsVO getEmergencyFileStatistics(@Valid EmergencyFileStatisticsParam param) {
        return this.emergencyFileStatisticsService.getEmergencyFileStatistics(param);
    }

}
