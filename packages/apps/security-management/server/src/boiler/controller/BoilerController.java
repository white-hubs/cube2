package com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.param.BoilerParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.param.BoilerStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.service.BoilerService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo.BoilerRecordPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo.BoilerResultVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BoilerController {

    private final BoilerService boilerService;

    /**
     * @Description 锅炉设备报警统计
     * @Author lrj
     */
    @GetMapping("/boiler-device-statistics")
    public Map<String, Object> getAlarmBimDeviceStatistics(BoilerStatisticsParam param) {
        return boilerService.getAlarmBimDeviceStatistics(param);
    }

    /**
     * @Description 数据概览
     * @Author lrj
     */
    @GetMapping("/data-overview")
    public Map<String, Object> getDataOverview(BoilerStatisticsParam param) {
        return boilerService.getDataOverview(param);
    }

    /**
     * @Description 温度
     * @Author lrj
     */
    @GetMapping("/data-temperature")
    public BoilerResultVo getDataTemperature(BoilerStatisticsParam param) {
        return boilerService.getDataTemperature(param);
    }

    /**
     * @Description 用水趋势
     * @Author lrj
     */
    @GetMapping("/data-water")
    public BoilerResultVo getDataWater(BoilerStatisticsParam param) {
        return boilerService.getDataWater(param);
    }

    /**
     * @Description 能耗趋势
     * @Author lrj
     */
    @GetMapping("/data-electricity")
    public BoilerResultVo getDataElectricity(BoilerStatisticsParam param) {
        return boilerService.getDataElectricity(param);
    }

    /**
     * @Description 锅炉记录
     * @Author lrj
     */
    @GetMapping("/boiler-data-record")
    public Pagination<BoilerRecordPageVo> getBoilerDataRecordPage(BoilerParam param) {
        return boilerService.getBoilerDataRecordPage(param);
    }

}
