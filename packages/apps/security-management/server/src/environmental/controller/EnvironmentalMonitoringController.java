package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.param.DateParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.service.EnvironmentalMonitoringService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.vo.EnvironmentalMonitoringVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnvironmentalMonitoringController {
    private final EnvironmentalMonitoringService environmentalMonitoringService;

    /**
     * @Description 隧道环境指标实时监测
     * @Author lrj
     */
    @GetMapping("/environmental-indicators")
    public Map<String, Object> getEnvironmentalIndicators() {
        return environmentalMonitoringService.getEnvironmentalIndicators();
    }

    /**
     * @Description 隧道CO浓度趋势分析
     * @Author lrj
     */
    @GetMapping("/trend-analysis-co-concentration-tunnels")
    public Map<String, Object> trendAnalysisCoConcentrationTunnels() {
        return environmentalMonitoringService.getCotRendAnalysis();
    }

    /**
     * @Description 实时隧道环境指标趋势分析
     * @Author lrj
     */
    @GetMapping("/trend-analysis-real-time/{paragraph}")
    public Map<String, Object> trendAnalysisRealTime(@PathVariable String paragraph) {
        return environmentalMonitoringService.trendAnalysisRealTime(paragraph);
    }

    /**
     * @Description 业务监测
     * @Author lrj
     */
    @GetMapping("/environmental-page")
    public Pagination<EnvironmentalMonitoringVo> getEnvironmentalPage(DateParam param) {
        return environmentalMonitoringService.getEnvironmentalPage(param);
    }

}
