package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.controller;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.DeviceHistoryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.StatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWaterElectricityService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWaterStatisticsService;
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
public class ColdWaterStatisticsController {
    private final ColdWaterStatisticsService coldWaterStatisticsService;

    private final ColdWaterElectricityService coldWaterElectricityService;

    /**
     * @Description 基础数据
     * @Author lrj
     */
    @GetMapping("/basic-data-report-analysis")
    public Map<String, Object> basicDataReportAnalysis() {
        return coldWaterStatisticsService.basicDataReportAnalysis();
    }

    /**
     * @Description 活跃商户
     * @Author lrj
     */
    @GetMapping("/merchant-activity-data")
    public Map<String, Object> getMerchantActivityData(StatisticsParam statisticsParam) {
        String timeFlag = statisticsParam.getTimeFlag();
        statisticsParam.setStartTime(timeFlag);
        statisticsParam.setEndTime(timeFlag);
        return coldWaterStatisticsService.merchantActivityData(statisticsParam);
    }

    /**
     * @Description 冷冻水使用数据
     * @Author lrj
     */
    @GetMapping("/chilled-water-data")
    public Map<String, Object> getChilledWaterData(StatisticsParam statisticsParam) {
        return coldWaterStatisticsService.getChilledWaterData(statisticsParam);
    }

    /**
     * @Description 设备详情历史数据比值
     * @Author lrj
     */
    @GetMapping("/device-history")
    public Map<String, Object> getDeviceHistory(DeviceHistoryParam param) {
        return coldWaterStatisticsService.getDeviceHistory(param);
    }

    /**
     * @Description 设备监测详情数历史属性分页
     * @Author lrj
     */
    @GetMapping("/device-attribute-data-history")
    public Pagination<KeyValueWithTsDTO> getDeviceAttributeDataHistory(DeviceHistoryParam param) {
        return coldWaterStatisticsService.getDeviceAttributeDataHistory(param);
    }

    /**
     * @Description 耗电统计
     * @Author lrj
     */
    @GetMapping("/cold-water-electricity")
    public Map<String, Object> getColdWaterElectricity(StatisticsParam param) {
        return coldWaterElectricityService.getColdWaterElectricity(param);
    }

}
